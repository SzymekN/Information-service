<script setup>
import TopNews from "./TopNews.vue";
import Article from "./MiniArticle.vue";
import { ref, onMounted, onUnmounted } from 'vue';

const articles = ref([]);
const page = ref(0);
const isLoading = ref(false);
const category = ref('')
var size = 5;
var fetchUrl = 'http://localhost:8080/client/articles/pages?';

const props = defineProps({
  category: { type: String, default: '' },
})

const fetchArticles = async () => {
    try {
      if (sessionStorage.getItem("allArticles") && articles.value.length < sessionStorage.getItem("allArticles").length) {
        const data = JSON.parse(sessionStorage.getItem("allArticles"))
        console.log(data.length)
        if (articles.value.length + size < data.length){
          articles.value = [...articles.value, ...data.slice(page.value * size, (page.value + 1) * size)];
          page.value++;
          isLoading.value = false;
          return;          
        }
      }
      const response = await fetch(fetchUrl+`page=${page.value}&size=${size}`);
      page.value++;
      const data = await response.json();
      articles.value = [...articles.value, ...data];
      sessionStorage.setItem("allArticles", JSON.stringify(articles.value))
      isLoading.value = false;
    } catch (error) {
      console.log(error);
    }
  };

  const stripTags = (htmlString) => {
    const div = document.createElement('div');
    div.innerHTML = htmlString;
    return div.textContent;
  }

  const handleScroll = () => {
    console.log(window.innerHeight + document.documentElement.scrollTop, document.documentElement.offsetHeight, isLoading.value)
    if (window.innerHeight + document.documentElement.scrollTop <= document.documentElement.offsetHeight || isLoading.value) {
      return;
    }
    isLoading.value = true;
    fetchArticles();
  }

  onMounted(() => {
    if (props.category != '')
      fetchUrl = fetchUrl + `category=${props.category}&`;
    sessionStorage.removeItem("allArticles");
    fetchArticles();
    size = 8;
    window.addEventListener('scroll', handleScroll);
  })

  onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll);
  })

  
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
      v-for="(item, index) in articles.slice(1)"
      :key="index"
      :articleTitle="item.title"
      :articleDescription="stripTags(articles[index].content)"
      :articleUrl="index+1"
      :imageUrl="getImage(item.content)"/>    

    <Article
      v-if="isLoading == true"
      v-for="i in 8" :key="i"
      :imageUrl="getImage(articles[0].content)"
      />
      
</template>