<script setup>
import TopNews from "@/components/TopNews.vue";
import Article from "@/components/MiniArticle.vue";
import { ref, onMounted } from 'vue';

const articles = ref([]);

const fetchArticles = async () => {
    try {
      const response = await fetch('http://localhost:8080/client/articles');
      const data = await response.json();
      console.log(data);
      articles.value = data;
      console.log(articles.value)
      localStorage.setItem("allArticles", JSON.stringify(data))
    } catch (error) {
      console.log(error);
    }
  };

  onMounted(fetchArticles);

  const stripTags = (htmlString) => {
    const div = document.createElement('div');
    div.innerHTML = htmlString;
    return (div.textContent.substring(0,100) || div.innerText.substring(0,100) || '')+'...';
  }

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
    
    <Article
      v-if="articles.length"
      v-for="(item, index) in articles.slice(1, 9)"
      :key="index"
      :articleTitle="item.title"
      :articleUrl="index+1"
      :imageUrl="getImage(item.content)"/>
      
</template>