<template>
  <router-link :to="`article?id=${articleUrl}&category=${articleCategory}`">
    <div :class="pageClass" class="article">
      <img :src="imageUrl" height="853" width="1200" alt="OBRAZ JEST NIEDOSTĘPNY"/>
      <div class="article_title">
        <p>{{ articleTitle }}</p>
        <div class="article_description"><p>{{ articleDescription }}</p></div>
      </div>
    </div>
  </router-link>
</template>

<script>
import { ref, onMounted } from 'vue';
import {getArticleById} from '@/scripts/Scripts.ts';

export default {
  props: {
    imageUrl: String,
    articleTitle: String,
    articleDescription: String,
    articleUrl: Number,
    category: String,
  },
  setup(props) {
    const pageClass = ref('');

    if(props.category && props.articleUrl){
      const sessionData = Object.values(JSON.parse(sessionStorage.getItem(props.category) || '[]'));
      const article = getArticleById(sessionData, props.articleUrl);
      var articleCategory=article[0].category;
    }

    onMounted(() => {
      if (props.category === 'politics' || props.category === 'science' || props.category === 'technology' || props.category === 'business') {
        pageClass.value = 'wider';
      } else {
      pageClass.value = 'other';
      }
    });

    return { pageClass,articleCategory};
  },
};
</script>

<style scoped>
.article {
  width: 23%;
  float: left;
  background: aliceblue;
  font-size: 1vmax;
  margin: 2% 2% 0 0;
  position: relative;
  text-align: center;
}

.wider {
  height: 13vmax;
}

.sport {
  height: 12vmax;
}

.other {
  height: 10vmax;
}

.article img {
  max-width: 100%;
  height: 100%;
  margin-right: 2%;
  display: block;
  transition: filter 0.5s ease;
}

.article img:hover {
  filter: brightness(80%);
}

.article_title {
  position: absolute;
  background:  rgba(0, 0, 0, 0.5);
  width: 100%;
  height: 45%;
  padding: 0 1% 0 1%;
  top: 77.5%;
  left: 50%;
  font-size: 1.25vmax;
  color: #fff;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transition: height 0.5s ease, transform 0.5s ease;
  overflow: hidden;
}

.article_title p {
  margin-top: 0.7rem;
}

.article:hover .article_title {
  height: 100%;
  transform: translate(-50%, -78%);
  display: block;
}

.article:hover .article_description {
  width: 100%; /* Make the description full width */
  margin: 0.5rem auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

.article_description p {
  width: 80%;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 5;
  text-overflow: ellipsis;
  overflow: hidden;
}

.article_title .article_description {
  display: none;
  font-size: 0.75vmax;
  text-align: justify;
  position: absolute; /* Add position absolute */
  top: 50%; /* Add top 50% */
  transform: translateY(-50%); /* Center vertically */
}

a {
  color: white;
  vertical-align: middle;
}

@media (max-width: 640px) {
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
