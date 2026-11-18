(async () => {
  const host = document.querySelector('[data-calendar]');
  if (!host) return;

  const dayInMs = 24 * 60 * 60 * 1000;
  const dayFormat = new Intl.DateTimeFormat('zh-CN', {dateStyle: 'long', timeZone: 'UTC'});
  const monthFormat = new Intl.DateTimeFormat('zh-CN', {month: 'numeric', timeZone: 'UTC'});
  const numberFormat = new Intl.NumberFormat('zh-CN');

  function waitUntilNearViewport() {
    if (!('IntersectionObserver' in window)) return Promise.resolve();
    return new Promise(resolve => {
      const observer = new IntersectionObserver(entries => {
        if (!entries.some(entry => entry.isIntersecting)) return;
        observer.disconnect();
        resolve();
      }, {rootMargin: '160px'});
      observer.observe(host);
    });
  }

  async function fetchDays(url) {
    const controller = new AbortController();
    const timeout = setTimeout(() => controller.abort(), 6000);
    try {
      const response = await fetch(url, {
        credentials: 'omit',
        referrerPolicy: 'no-referrer',
        signal: controller.signal
      });
      if (!response.ok) throw new Error('HTTP ' + response.status);

      const {contributions} = await response.json();
      if (!Array.isArray(contributions) || contributions.length < 365 || contributions.length > 371) {
        throw new Error('贡献日期范围无效');
      }
      const result = contributions.map(({date, count, level}) => ({
        date: new Date(date + 'T00:00:00Z'), count, level
      }));
      const first = result[0].date;
      if (first.getUTCDay() !== 0 || result.some(({date, count, level}, index) =>
        date.getTime() !== first.getTime() + index * dayInMs ||
        !Number.isInteger(count) || count < 0 || !Number.isInteger(level) || level < 0 || level > 4)) {
        throw new Error('每日贡献数据无效');
      }
      return result;
    } finally {
      clearTimeout(timeout);
    }
  }

  let days;
  let sourceLabel = '公开贡献快照';
  const liveUrl = host.dataset.contributionsUrl;
  if (liveUrl) {
    host.querySelector('[data-calendar-loading]').hidden = false;
    await waitUntilNearViewport();
    try {
      days = await fetchDays(liveUrl);
      sourceLabel = 'GitHub 公开贡献';
    } catch (error) {
      console.warn('GitHub 贡献加载失败，尝试显示本地快照', error);
      sourceLabel = '在线数据暂不可用 · 公开贡献快照';
    }
  }
  if (!days && typeof githubContributionSnapshot !== 'undefined') {
    const {start, counts, levels} = githubContributionSnapshot;
    const startTime = Date.parse(start + 'T00:00:00Z');
    days = counts.map((count, index) => ({
      count, level: levels[index], date: new Date(startTime + index * dayInMs)
    }));
  }
  if (!days) {
    host.textContent = '贡献数据暂不可用，可以前往 GitHub 查看完整日历。';
    return;
  }

  const lastDay = days[days.length - 1];

  host.innerHTML = `
    <div class="calendar">
      <div class="calendar-meta">
        <div class="calendar-totals">
          <div><strong class="calendar-number" data-total></strong><span>次贡献</span></div>
          <div><strong class="calendar-number" data-active></strong><span>天有贡献</span></div>
        </div>
        <div class="calendar-range" role="group" aria-label="贡献日期范围">
          <button type="button" data-range="year" aria-pressed="true">过去一年</button>
          <button type="button" data-range="quarter" aria-pressed="false">近 13 周</button>
        </div>
      </div>
      <div class="calendar-body">
        <div class="calendar-matrix">
          <div class="calendar-weekdays" aria-hidden="true"><span>一</span><span>三</span><span>五</span></div>
          <div class="calendar-scroll">
            <div class="calendar-track">
              <div class="calendar-months" aria-hidden="true"></div>
              <div class="calendar-grid" role="group" aria-label="每日 GitHub 贡献，使用方向键查看日期"></div>
            </div>
          </div>
        </div>
        <div class="calendar-key">
          <output aria-live="off"></output>
          <span class="calendar-legend" aria-label="贡献数量从少到多，共五档" role="img">
            少 <i data-level="0"></i><i data-level="1"></i><i data-level="2"></i><i data-level="3"></i><i data-level="4"></i> 多
          </span>
        </div>
        <p class="calendar-source"></p>
      </div>
    </div>
  `;

  const controls = [...host.querySelectorAll('[data-range]')];
  const months = host.querySelector('.calendar-months');
  const grid = host.querySelector('.calendar-grid');
  const scroller = host.querySelector('.calendar-scroll');
  const track = host.querySelector('.calendar-track');
  const output = host.querySelector('output');
  const total = host.querySelector('[data-total]');
  const active = host.querySelector('[data-active]');
  host.querySelector('.calendar-source').textContent = sourceLabel + ' · 截至 ' + dayFormat.format(lastDay.date);

  let cells = [];
  let selected = 0;
  let focusIndex = 0;

  function select(index) {
    cells[selected]?.setAttribute('aria-pressed', 'false');
    selected = index;
    cells[selected].setAttribute('aria-pressed', 'true');
    output.textContent = cells[selected].getAttribute('aria-label');
  }

  function moveFocus(index) {
    cells[focusIndex].tabIndex = -1;
    focusIndex = index;
    cells[focusIndex].tabIndex = 0;
  }

  function render(quarter) {
    // Each week is a Sunday-first column, including the partial current week.
    const weeks = Math.ceil(days.length / 7);
    const first = quarter ? Math.max(0, (weeks - 13) * 7) : 0;
    const period = days.slice(first);
    track.style.setProperty('--weeks', String(Math.ceil(period.length / 7)));
    total.textContent = numberFormat.format(period.reduce((sum, day) => sum + day.count, 0));
    active.textContent = numberFormat.format(period.filter(day => day.count > 0).length);
    months.replaceChildren();
    cells = period.map(({date, count, level}, index) => {
      const column = Math.floor(index / 7) + 1;
      // Leave room for the next month label when the first month is partial.
      if (date.getUTCDate() === 1 || (index === 0 && period[20]?.date.getUTCMonth() === date.getUTCMonth())) {
        const month = document.createElement('span');
        month.textContent = monthFormat.format(date);
        month.style.gridColumn = String(column);
        months.appendChild(month);
      }

      const button = document.createElement('button');
      button.type = 'button';
      button.className = 'activity-cell';
      button.dataset.level = level;
      button.dataset.index = String(index);
      button.setAttribute('aria-label', dayFormat.format(date) + ' · ' + numberFormat.format(count) + ' 次贡献');
      button.setAttribute('aria-pressed', 'false');
      button.tabIndex = -1;
      return button;
    });
    grid.replaceChildren(...cells);
    focusIndex = 0;
    selected = 0;
    moveFocus(cells.length - 1);
    select(cells.length - 1);
    scroller.scrollLeft = scroller.scrollWidth;
  }

  const direction = {ArrowUp: -1, ArrowDown: 1, ArrowLeft: -7, ArrowRight: 7};
  grid.addEventListener('keydown', event => {
    if (!(event.key in direction) || !event.target.matches('.activity-cell')) return;
    event.preventDefault();
    const index = Number(event.target.dataset.index);
    const next = Math.min(cells.length - 1, Math.max(0, index + direction[event.key]));
    cells[next].focus();
  });
  grid.addEventListener('focusin', ({target}) => {
    if (!target.matches('.activity-cell')) return;
    const index = Number(target.dataset.index);
    moveFocus(index);
    output.textContent = target.getAttribute('aria-label');
  });
  grid.addEventListener('click', ({target}) => {
    if (target.matches('.activity-cell')) select(Number(target.dataset.index));
  });
  grid.addEventListener('pointerover', ({target}) => {
    if (target.matches('.activity-cell')) output.textContent = target.getAttribute('aria-label');
  });
  grid.addEventListener('pointerleave', () => {
    output.textContent = cells[selected].getAttribute('aria-label');
  });
  controls.forEach(control => {
    control.addEventListener('click', () => {
      if (control.getAttribute('aria-pressed') === 'true') return;
      controls.forEach(other => other.setAttribute('aria-pressed', String(other === control)));
      render(control.dataset.range === 'quarter');
    });
  });
  render(false);
})();
