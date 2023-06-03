<script setup>
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import '@vueup/vue-quill/dist/vue-quill.core.css';
import  RecommendedArticles from './RecommendedArticles.vue'
import { ref, onMounted,watch} from 'vue'
import { useRoute,useRouter} from 'vue-router'
import { getArticleById } from '@/scripts/Scripts.ts'

const route = useRoute();
const router = useRouter();
const content = ref('')
const title = ref('')
var article = [];
const props = defineProps({
  content: { type: String, default: 'param' },
  title: { type: String, default: 'Tytuł' },
})


onMounted(() => {
  let category = route.query["category"];
  let index = route.query["id"];
  const sessionData = Object.values(JSON.parse(sessionStorage.getItem(category) || '[]'));
  // console.log(index)
  // console.log(sessionData)
  article =getArticleById(sessionData,index);
  
  if (article.length > 0) {
    content.value = article[0].content;
    title.value = article[0].title;
  }
});
// Watch for changes in the route query when recommended article is clicked
watch(() => route.query, () => {
    router.go();
  }
);
</script>

<template>

    <h1 class="done-article">{{title}}</h1>
    <div id="justText" class="content ql-editor" v-html="content"></div>
  <div class="suggested-articles">
    <RecommendedArticles v-if="article[0]" :currentArticleId=article[0].id ></RecommendedArticles>
  </div>
</template>

<style>
.done-article {
  text-transform: uppercase;
  margin-left: 2%;
}
.suggested-articles {
  width: 23%;
  margin-left: 2%;
  float: right;
  min-height: 100px;
  background: white;
  border-radius: 10px;
  padding: 2%;
  box-shadow: 2px 2px 20px -10px;
}
.content > h2, .content > h1 {
  width: 100%;
  margin-bottom: 1%;
}

.content > p > img {
  max-width: 50%;
  margin: 2% 25% 2% 25%;
}

.content {
  background: white;
  border-radius: 10px;
  width: 75%;
  float: left;
  padding: 2%;
  box-shadow: 2px 2px 20px -10px;
}

</style>