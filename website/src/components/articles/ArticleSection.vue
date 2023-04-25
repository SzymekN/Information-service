<script setup>
import TopNews from "./TopNews.vue";
import MiniArticle from "./MiniArticle.vue";
import { ref, onMounted } from 'vue';

const articles = ref([]);

const fetchArticles = async () => {
    try {
      const response = await fetch('http://localhost:8080/client/articles');
      const data = await response.json();
      console.log(data);
      articles.value = data;
      console.log(articles.value)
      sessionStorage.setItem("allArticles", JSON.stringify(data))
    } catch (error) {
      console.log(error);
    }
  };

  const stripTags = (htmlString) => {
    const div = document.createElement('div');
    div.innerHTML = htmlString;
    return div.textContent;
  }

  onMounted(fetchArticles);
  
  const getImage = (htmlString) => {
    const parser = new DOMParser();
    const doc = parser.parseFromString(htmlString, 'text/html');
    const img = doc.querySelector('img');
    if(img == null || img == undefined){
      return "/17-g-ry-dla-seniora-jak-si.jpg"
    }
    const src = img.getAttribute("src");
    return src;
    }

</script>

<template>
    <TopNews 
      v-if="articles.length"
      :articleTitle="articles[0].title"
      :articleDescription="stripTags(articles[0].content)"
      :articleUrl="0"
      :imageUrl="getImage(articles[0].content)"/>
    
    <MiniArticle
      v-if="articles.length"
      v-for="(item, index) in articles.slice(1, 9)"
      :key="index"
      :articleTitle="item.title"
      :articleDescription="stripTags(articles[index].content)"
      :articleUrl="index+1"
      :imageUrl="getImage(item.content)"/>
      
</template>