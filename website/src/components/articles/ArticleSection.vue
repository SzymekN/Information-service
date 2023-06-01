<script setup>
import TopNews from "./TopNews.vue";
import MiniArticle from "./MiniArticle.vue";
import { ref, onMounted, onUnmounted } from 'vue';
import { getArticleById } from '@/scripts/Scripts.ts'
const articles = ref([]);
const page = ref(0);
const isLoading = ref(false);
// const category = ref('')
var size = 9;
var fetchUrl = '/client/articles/pages?';

const props = defineProps({
  category: { type: String, default: 'top' },
})

const fetchArticles = async () => {
    try {

      //read from session storage instead of making request
      if (sessionStorage.getItem(props.category) && articles.value.length < JSON.parse(sessionStorage.getItem(props.category)).length) {
        const data = JSON.parse(sessionStorage.getItem(props.category))
        console.log(data.length)
        if (articles.value.length + size < data.length){
          articles.value = [...articles.value, ...data.slice(page.value * size, (page.value + 1) * size)];
          page.value++;
          isLoading.value = false;
          return;          
        }
      }

      console.log(fetchUrl+`page=${page.value}&size=${size}`)
      const response = await fetch(fetchUrl+`page=${page.value}&size=${size}`);
      page.value++;
      const data = await response.json();
      articles.value = [...articles.value, ...data];
      sessionStorage.setItem(props.category, JSON.stringify(articles.value));
      
      isLoading.value = false;

      const articlesWithImage = data.map((article) => {
      return {
        ...article,
        image: getImage(article.content)
      };
    });

   
    articlesWithImage.forEach((article) => {
      const category = article.category;
      const categoryArticles = JSON.parse(sessionStorage.getItem(category)) || [];
      
      // If article is already in session storage, don't add it again
      if(getArticleById(categoryArticles,article.id).length == 0){
        categoryArticles.push(article);
        sessionStorage.setItem(category, JSON.stringify(categoryArticles));
      }
    });
    
      localStorage.setItem('articles', JSON.stringify(articlesWithImage));
      
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
    // console.log(window.innerHeight + document.documentElement.scrollTop, document.documentElement.offsetHeight, isLoading.value)
    if (window.innerHeight + document.documentElement.scrollTop <= document.documentElement.offsetHeight || isLoading.value) {
      return;
    }
    isLoading.value = true;
    fetchArticles();
  }

  onMounted(() => {
    if (props.category !== 'top')
      fetchUrl = fetchUrl + `category=${props.category}&`;

    sessionStorage.removeItem(props.category) //TODO: remove to read from session storage
    fetchArticles();
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
      :articleUrl=articles[0].id
      :imageUrl="getImage(articles[0].content)"
      :category="props.category"/>
    
      <MiniArticle
      v-if="articles.length"
      v-for="(item, index) in articles.slice(1)"
      :key="index"
      :articleTitle="item.title"
      :articleDescription="stripTags(articles[index].content)"
      :articleUrl="item.id"
      :imageUrl="getImage(item.content)"
      :category="props.category"/>

    <MiniArticle
      v-if="isLoading == true"
      v-for="i in 8" :key="i"
      :imageUrl="getImage(articles[0].content)"
      />
      
</template>