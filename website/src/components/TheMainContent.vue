<script setup>
import TopNews from "@/components/TopNews.vue";
import MiniNews from "@/components/MiniArticle.vue";
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
  const stripLargerTags = (htmlString) => {
    const div = document.createElement('div');
    div.innerHTML = htmlString;
    return (div.textContent.substring(0,200) || div.innerText.substring(0,100) || '')+'...';
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
  <div class="section_content">

    <p class = "section_title">O TYM SIĘ MÓWI</p>
    <hr>
    <TopNews 
      v-if="articles.length"
      :articleTitle="articles[0].title"
      :articleDescription="stripTags(articles[0].content)"
      :articleUrl="0"
      :imageUrl="getImage(articles[0].content)"/>
    
    <MiniNews
      v-if="articles.length"
      v-for="(item, index) in articles.slice(1, 9)"
      :key="index"
      :articleTitle="item.title"
      :articleDescription="stripLargerTags(articles[index].content)"
      :articleUrl="index+1"
      :imageUrl="getImage(item.content)"/>

  </div>

  <div class="section_content">

    <p class = "section_title">EDUKACJA</p>
    <hr>
    <TopNews/>
    <MiniNews/>
    <MiniNews/>
    <MiniNews/>
    <MiniNews/>
    <MiniNews/>
    <MiniNews/>
    <MiniNews/>
    <MiniNews/>

  </div>

</template>

<style>
.section_title {
  font-size: 2rem;
  font-weight: 500;
  margin-bottom: -1%;
}

.section_content {
  float: left;
}

@media (max-width: 640px){
  .section_content:first-child {
    margin-top: 6rem;
  }
}
</style>