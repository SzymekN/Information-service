<template>
  <router-link :to="`article?id=${articleUrl}`">
  <div class="top">
      <img :src="imageUrl" height="853" width="1200"/>
      <!-- <img src="/17-g-ry-dla-seniora-jak-si.jpg" height="853" width="1200"/> -->
      <div class="article_title">
            <h1>{{articleTitle}}</h1>
            <p>{{articleDescription}}</p>
          </div>
        </div>
  </router-link>
</template>

<script>
export default {
  props: {
    imageUrl: String,
    articleTitle: String,
    articleDescription: String,
    articleUrl: Number,
  },
};
</script>

<style scoped>
.top {
  width: 48%;
  float: left;
  margin: 2% 2% 0 0;
  background: aliceblue;
  font-size: 1vmax;
}

.top img {
  float: left;
  max-width: 100%;
  max-height: 21.5vmax;
}

.top img:hover {
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
  .top {
    width: 98%;
  }

  .top img {
    max-height: 35vmax;
  }

  .article_title {
    font-size: 2vmax;
  }
}
</style>