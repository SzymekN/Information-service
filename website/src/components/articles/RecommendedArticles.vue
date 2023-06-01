<template>
    <div>
      <h2>Proponowane artykuły</h2>
      <div class="recommended-articles">
        <div v-for="article in recommendedArticles" :key="article.id" class="article-container">
            <router-link class="recommendedLink" :to="`article?id=${article.id}&category=${article.category}`">
            <div class="image-container">
                <img :src="article.image" :alt="article.title" />
            </div>
            <div class="title-container">
                <h3>{{ article.title }}</h3>
            </div>
            </router-link>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, onMounted,watch } from 'vue';
  import { useRoute } from 'vue-router';
  export default {
    setup() {
      const recommendedArticles = ref([]);
      const route = useRoute();
      const getRandomItems = (array, count) => {
        const shuffled = array.slice(0);
        let i = array.length;
        let temp;
        let index;
  
        while (i--) {
          index = Math.floor((i + 1) * Math.random());
          temp = shuffled[index];
          shuffled[index] = shuffled[i];
          shuffled[i] = temp;
        }
  
        return shuffled.slice(0, count);
      };
  
      onMounted(() => {
        // Retrieve all articles from local storage
        const allArticles = JSON.parse(localStorage.getItem('articles')) || [];
  
        // Select random 10 articles
        const randomArticles = getRandomItems(allArticles, 10);
  
        // Set recommendedArticles with the random articles
        recommendedArticles.value = randomArticles;
      });
  
      return {
        recommendedArticles
      };
    }
  };
  </script>
  
  <style>
 
.recommended-articles {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  max-width: 30%;
  float:right;
  overflow: hidden;
}

.article-container {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.image-container {
  width: 95px;
  height: 59px;
  margin-right: 10px;
  float:left;
}

.image-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.title-container {
  display: flex;
  justify-content: center;
}

.title-container h3 {
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100%;
  word-wrap: break-word;
  margin-top: 0;
  font-size: small;
}


</style>