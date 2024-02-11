//配置集中到一个统一的地方，以便在整个应用程序中共享。
//在前端项目中，常见的做法是创建一个配置文件或模块，将这些配置集中管理。
//我设置了一个 Axios 实例，配置了基础 URL，并添加了一个简单的响应拦截器来处理错误。
//你可以通过这个实例发送 HTTP 请求，并根据需要自定义其他配置。
const axiosInstance = axios.create();

// 添加响应拦截器
axiosInstance.interceptors.response.use(
    (response) => {
        let res = response.data;
        if(res.code === 20001){
            Vue.prototype.$message.error(res.message);
        }
        // 在这里对响应数据进行处理
        return res;
    },
    (error) => {
        console.error('Axios请求出错:', error);
        return Promise.reject(error);
    }
);


