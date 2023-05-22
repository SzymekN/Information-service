<template>
  <router-link :to="`article?id=${articleUrl}&category=${category}`">
  <div class="top">
      <img :src="imageUrl" height="853" width="1200"/>
      <!-- <img src="/17-g-ry-dla-seniora-jak-si.jpg" height="853" width="1200"/> -->
      <div class="article_title">
            <h1>{{articleTitle}}</h1>
            <div class="article_description"><p>{{articleDescription}}</p></div>
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
    category: String,
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

.top:hover .article_title {
  height: 100%;
  transform: translate(-50%, -78%);
  display: block;
}
.top:hover .article_description p{
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 10;
  text-overflow: ellipsis;
  overflow: hidden;
  height: 100%;
}
.top img {
  float: left;
  max-width: 100%;
  max-height: 21.5vmax;
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
  transition: height 0.5s ease, transform 0.5s ease;
  margin: 0 auto;
}
.article_title h1{
  margin-bottom: 0;
}

.article_description{
  display: flex;
  justify-content: center;
  align-items: center;
}
.article_description p{
  width: 80%;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  text-overflow: ellipsis;
  overflow: hidden;
  margin-top:0;
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