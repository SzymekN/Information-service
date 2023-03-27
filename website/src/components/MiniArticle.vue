<template>
  <div class="article">
    <a href="/client/src/main/ui/public">
      <img src="/17-g-ry-dla-seniora-jak-si.jpg" height="853" width="1200" alt="OBRAZ JEST NIEDOSTĘPNY"/>
    </a>
    <div class="article_title">
      <a href="/client/src/main/ui/public">
        <p>Tytuł artykułu</p>
      </a>
    </div>

  </div>
</template>

<script>

</script>

<style scoped>

.article {
  width: 23%;
  float: left;
  background: aliceblue;
  font-size: 1vmax;
  margin: 2% 2% 0 0;
}

.article img {
  max-width: 100%;
  max-height: 10vmax;
  margin-right: 2%;
  display: block;
}

.article img:hover {
  filter: brightness(80%);
}

.article_title {
  position: absolute;
  background:  rgba(0, 0, 0, 0.5);
  width: 100%;
  height: 45%;
  text-align: center;
  padding: 0 1% 0 1%;
  top: 77.5%;
  left: 50%;
  font-size: 1vmax;
  color: #fff;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  margin: 0 auto;
}

a {
  color: white;
  vertical-align: middle;
}

@media (max-width: 640px){
  .article {
    width: 48%;
  }

  .article img {
    max-height: 18.5vmax;
  }

  .article_title {
    font-size: 2vmax;
  }
}

</style>