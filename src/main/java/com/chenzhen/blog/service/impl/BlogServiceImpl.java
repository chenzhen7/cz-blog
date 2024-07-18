package com.chenzhen.blog.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenzhen.blog.entity.dto.BlogDTO;
import com.chenzhen.blog.entity.pojo.BlogTag;
import com.chenzhen.blog.entity.pojo.Tag;
import com.chenzhen.blog.entity.pojo.Blog;
import com.chenzhen.blog.entity.query.BlogQuery;
import com.chenzhen.blog.entity.vo.BlogVO;
import com.chenzhen.blog.entity.mapper.TagMapper;
import com.chenzhen.blog.sdk.CommonResult;
import com.chenzhen.blog.sdk.csdn.BizClient;
import com.chenzhen.blog.sdk.csdn.GetArticleRequest;
import com.chenzhen.blog.sdk.csdn.GetArticleResp;
import com.chenzhen.blog.service.BlogService;
import com.chenzhen.blog.entity.mapper.BlogMapper;
import com.chenzhen.blog.service.TypeService;
import com.chenzhen.blog.util.MarkdownUtil;
import com.chenzhen.blog.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author MIS
* @description 针对表【t_blog】的数据库操作Service实现
* @createDate 2022-09-11 18:21:11
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService{


    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public PageInfo<BlogVO> pageAdminBlogs(BlogQuery query) {
        PageHelper.startPage(query.getPageNum(), 10);
        PageHelper.orderBy("create_time desc");

        List<BlogVO> blogList = blogMapper.pageAdminBlogs(query);
        if (CollUtil.isEmpty(blogList)){
            return new PageInfo<>(blogList);
        }
        // 获取所有博客的 ID 列表
        List<Long> blogIds = blogList.stream().map(BlogVO::getId).collect(Collectors.toList());
        // 根据博客 ID 列表查询所有对应的标签
        List<BlogTag> blogTags = tagMapper.getTagsByBlogIds(blogIds);
        // 设置每个博客的标签
        for (BlogVO blog : blogList) {
            for (BlogTag blogTag : blogTags){
                if (blogTag.getBlogId().equals(blog.getId())){
                    blog.setTagList(blogTag.getTags());
                }
            }
        }
        return new PageInfo<>(blogList);

    }

    @Override
    public R saveBlog(BlogDTO blogDTO) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDTO, blog);
        if (!save(blog)) {
            return R.error("保存文章信息失败!");
        }
        return saveBlogTags(blog.getId(), blogDTO.getTagIds()) ? R.success() : R.error("保存文章标签失败!");
    }

    @Override
    public BlogDTO getBlogDTO(Long id) {
        Blog blog = blogMapper.selectById(id);
        if (blog == null){
            return null;
        }
        BlogDTO blogDTO = new BlogDTO();
        BeanUtils.copyProperties(blog, blogDTO);

        List<Tag> tagList = tagMapper.getTagsByBlogId(blog.getId());
        //设置标签ID列表
        List<Long> tagIds = new ArrayList<>();
        tagList.forEach(tag -> {
            tagIds.add(tag.getId());
        });
        blogDTO.setTagIds(tagIds);

        return blogDTO;
    }

    @Override
    @Transactional
    public R updateBlog(BlogDTO blog) {
        //更新博客
        if (blogMapper.updateById(blog) <= 0) {
            return R.error("更新博客信息失败!");
        }
        if (!updateBlogTags(blog.getId(), blog.getTagIds())) {
            return R.error("更新博客标签失败!");
        }
        //更新博客的标签
        return  R.success() ;
    }

    @Override
    public PageInfo<BlogVO> pageTagsBlog(long[] tagIds, Integer pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<BlogVO> blogList;
        // 如果没有标签Id
        if (ArrayUtil.isEmpty(tagIds)) {
            // 获取所有文章
            blogList  = getBaseMapper().getBlogList();
            PageInfo<BlogVO> pageInfo = new PageInfo<>(blogList);
            return pageInfo;

        }else{
            // 根据标签Id列表获取文章Id列表
            List<Long> blogIds = getBaseMapper().getBlogIdsByTagIds(tagIds);
            if (ArrayUtil.isEmpty(blogIds)) {
                return new PageInfo<>(new ArrayList<>());
            }
            //这一步目前仅用于获取分页信息，实际的数据列表还未设置。
            PageInfo pageInfo = new PageInfo(blogIds);
            //根据获取到的文章ID列表查询具体的博客列表 blogList。
            blogList = getBaseMapper().getBlogListByBlogIds(blogIds);
            //将查询得到的博客列表设置到之前创建的 pageInfo 对象中，这样 pageInfo 对象就包含了分页信息和具体的博客列表数据。
            pageInfo.setList(blogList);

            return pageInfo;
        }

    }

    @Override
    public List<Blog> searchBlog(String keyword) {
        if (StrUtil.isBlank(keyword)) {
            return null;
        }
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Blog::getTitle, keyword);
        return list(wrapper);
    }

    @Override
    public List<Blog> getSimilarBlogs(Long id) {
        Blog targetBlog  = getById(id);
        String targetTitle = targetBlog.getTitle();

        Map<Blog,Double> recommendedBlogsMap = new HashMap<>();
        // 遍历所有博文，计算标题相似度
        for (Blog blog : blogMapper.getAllTitle()) {
            // 排除目标博文自身
            if (!blog.getId().equals(targetBlog.getId())) {
                double similarity = calculateCosineSimilarity(targetTitle, blog.getTitle());
                // 记录相似度
                recommendedBlogsMap.put(blog, similarity);
            }
        }
        // 对相似度进行降序排序
        List<Blog> recommendedBlogs = recommendedBlogsMap.entrySet().stream()
                .sorted(Map.Entry.<Blog, Double>comparingByValue().reversed())
                .limit(3)  // 取前三篇
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return recommendedBlogs;
    }

    @Override
    public void syncCsdn(List<Integer> ids,Long typeId) {
        for (Integer id : ids) {
            syncBlog(typeId, id);
        }
    }

    private void syncBlog(Long typeId, Integer id){
        try{
            GetArticleRequest getArticleRequest = GetArticleRequest.builder()
                    .id(id)
                    .cookie("cookies")
                    .build();
            GetArticleResp getArticleResp = BizClient.getArticle(getArticleRequest).getResult();

            Blog blog = new Blog();
            blog.setTitle(getArticleResp.getTitle());
            blog.setTypeId(typeId);
            blog.setContent(getArticleResp.getMarkdown_content());
            blog.setDescription(getArticleResp.getDescription());
            blog.setFirstPicture(CollUtil.isEmpty(getArticleResp.getCover_image()) ? null : getArticleResp.getCover_image().get(0));
            blog.setCommentabled(true);
            blog.setAppreciation(true);
            blog.setPublished(true);
            blog.setRecommend(true);
            blog.setShareStatement(true);
            blog.setCopyright(1);

            save(blog);
        }catch (Exception e){
            log.error("同步博客失败",e);
            e.printStackTrace();
        }
    }

    public static double calculateCosineSimilarity(String text1, String text2) {
        Map<Character, Integer> wordCount1 = getWordCount(text1);
        Map<Character, Integer> wordCount2 = getWordCount(text2);

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (Character word : wordCount1.keySet()) {
            int count1 = wordCount1.get(word);
            int count2 = wordCount2.getOrDefault(word, 0);
            dotProduct += count1 * count2;
            magnitude1 += Math.pow(count1, 2);
        }

        for (Integer count : wordCount2.values()) {
            magnitude2 += Math.pow(count, 2);
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 == 0.0 || magnitude2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }

    private static Map<Character, Integer> getWordCount(String text) {
        Map<Character, Integer> wordCount = new HashMap<>();
        for (char c : text.toCharArray()) {
            wordCount.put(c, wordCount.getOrDefault(c, 0) + 1);
        }
        return wordCount;
    }


    public Boolean updateBlogTags(Long blogId, List<Long> tagIds) {
        //删除原有关联关系
        blogMapper.deleteAllBlogTags(blogId);
        //没有设置标签 直接返回
        if (CollUtil.isEmpty(tagIds)){
            return true;
        }
        //插入新的关联关系
        blogMapper.saveBlogTags(blogId, tagIds);
        return true;

    }

    public Boolean saveBlogTags(Long id, List<Long> tagIds) {
        return blogMapper.saveBlogTags(id, tagIds);
    }

    @Override
    public PageInfo<BlogVO> pageIndex(Long typeId, Integer pageNum, Integer pageSize) {
        //开启分页
        PageHelper.startPage(pageNum, pageSize);
        PageHelper.orderBy("create_time desc");
        //1 获取该分类下的所有博文列表 typeId为空则获取所有
        List<BlogVO> blogList = blogMapper.pageBlogs(typeId);
        if (blogList.isEmpty()) {
            return new PageInfo<>(blogList, 5);
        }
        //2 获取所有博客的 ID 列表
        List<Long> blogIds = blogList.stream().map(BlogVO::getId).collect(Collectors.toList());
        //3 根据博客 ID 列表查询所有对应的标签
        List<BlogTag> blogTags = tagMapper.getTagsByBlogIds(blogIds);
        //4 设置每个博客的标签
        for (BlogVO blog : blogList) {
            for (BlogTag blogTag : blogTags){
                if (blogTag.getBlogId().equals(blog.getId())){
                    blog.setTagList(blogTag.getTags());
                }
            }
        }
        PageInfo<BlogVO> pageInfo = new PageInfo<>(blogList, 5);
        return pageInfo;

    }



    @Override
    public List<BlogVO> getRecommendList() {
        List<BlogVO> recommendList = getBaseMapper().getRecommendList();
        return recommendList;
    }


    @Transactional
    @Override
    public BlogVO getBlogDetail(Long id) {

        Blog blog = getOne(new LambdaQueryWrapper<Blog>().eq(Blog::getId, id).eq(Blog::getPublished, 1));
        //属性复制
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blog, blogVO);


        //获得博文标签
        List<Tag> tags = tagMapper.getTagsByBlogId(blog.getId());
        //设置标签
        blogVO.setTagList(tags);
        //Markdown语法转html
        String html = MarkdownUtil.markdownToHtmlExtensions(blog.getContent());
        blogVO.setContent(html);
        //更新浏览次数+1
        getBaseMapper().updateViews(blog.getId());

        return blogVO;
    }



    @Override
    public Long getBlogViewTotal() {
        Long countViews = getBaseMapper().countViews();

        return countViews;
    }




}




