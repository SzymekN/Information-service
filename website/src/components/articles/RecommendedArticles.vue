<template>
    <div>
      <h2 class="proposed-header">Proponowane artykuły</h2>
      <div class="recommended-articles">
        <div v-for="article in recommendedArticles" :key="article.id" class="article-container" >
            <router-link class="recommendedLink" :to="`article?id=${article.id}&category=${article.category}`">
            <div class="image-container">
                <img :src="article.image" :alt="article.title" />
            </div>
            <div class="title-container">
                <p class="recommended-title">{{ article.title }}</p>
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
    props:{
      currentArticleId: Number,
    },
    setup(props) {
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
  
        // Filter out the article with the matching ID
        const filteredArticles = allArticles.filter(article => article.id !== props.currentArticleId);

        // Select random 10 articles from the filtered list
        const randomArticles = getRandomItems(filteredArticles, 10);
          
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
  float:right;
  overflow: hidden;
}

.article-container {
  display: flex;
  align-items: flex-start;
  padding-bottom: 10px;
  min-width: 100%;
}


a.router-link-active.router-link-exact-active.recommendedLink{
  min-width: 100%;
  box-shadow: 2px 15px 10px -17px;
  padding: 2%;
}

 a.router-link-active.router-link-exact-active.recommendedLink:hover{
   background: aliceblue;
    border-radius: 10px;
   box-shadow: 2px 2px 20px -10px;
 }


.recommended-title {
  font-size: 1vmax;
  font-weight: 500;
}
.article-container:last-child {
  border-bottom: none; 
}

.image-container {
  width: 45%;
  height: 5vmax;
  margin: 3% 3% 3% 0%;
  float: left;
}

.image-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.title-container {
  display: flex;
  justify-content: left;
}

.title-container h3 {
  min-width: 100%;
  max-width: 5%;
  margin-top: 0;
  font-size: small;
  width:100%;
}

.proposed-header {
  text-transform: uppercase;
  margin-block:0;
  margin-bottom: 10px;
}
</style>