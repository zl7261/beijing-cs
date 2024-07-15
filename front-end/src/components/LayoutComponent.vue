<template>
  <div class="layout-wrapper">
    <header class="header">北京大学长沙计算与数字经济研究院</header>
    <aside class="aside-left">Excel管理</aside>
    <main class="main">
      <slot name="default"/>
    </main>
    <footer class="footer">杨广 全栈工程师</footer>
  </div>
</template>

<style>

.layout-wrapper {
  display: grid;
  grid-template-areas:
          "header header"
          "aside-left main"
          "footer footer";
  grid-template-columns: 200px 1fr;
  grid-template-rows: 50px 1fr 50px;
  min-height: 100vh;
  width: 100%;
}

.header {
  grid-area: header;
}

.footer {
  grid-area: footer;
}

.main {
  grid-area: main;
}

.aside-left {
  grid-area: aside-left;
}


.header,
.footer {
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  background: #444;
}

.aside-left {
  padding: 30px;
  background: #eee;
}

.main {
  padding: 30px;
  background: #fff;
}

@media screen and (max-width: 600px) {
  .layout-wrapper {
    grid-template-areas:
            "header"
            "aside-left"
            "main"
            "footer";
    grid-template-columns: 100%;
    grid-template-rows: 50px 50px minmax(300px, auto) 50px 80px;
  }

  .aside-left,
  .main {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

</style>
