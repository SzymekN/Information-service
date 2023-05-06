<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import '@vueup/vue-quill/dist/vue-quill.core.css';
import { ref, reactive, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const quillEditor = ref()
const content = ref("")
const title = ref("Tytuł artykułu")
const category = ref("politics")
const articleReady = ref(false)
const options = reactive({
    theme: 'snow',
    placeholder: 'Zacznij pisać artykuł...',
})


// TODO: 
// - autosave every n minutes/seconds,
// - button to add gallery to the article
function updateContext(){

  var text = quillEditor.value.getHTML();
  // console.log(text)

}

function quillReady(){
  console.log("quill ready")
}

onMounted(() => {
  console.log("mounted")
  let route = useRoute();
  let redirected = route.query["redirected"]
  if (redirected != undefined){
    content.value = JSON.parse(sessionStorage.getItem("articleToEdit")).content
    title.value = JSON.parse(sessionStorage.getItem("articleToEdit")).title
  }
  // quillEditor.value.focus()
})

</script>

<template>

  <h1>Edytor</h1>
  <!-- TODO: add topic proposal assigned to the article -->
  <label for="title">Tytuł artykułu:</label>
  <input type="text" v-model="title" placeholder="Tytuł artykułu" class="title_input"><br/>
  <label for="category">Wybierz kategorię:</label>
  <select v-model="category" class="category_input">
    <option value="politics">Polityka</option>
    <option value="business">Biznes</option>
    <option value="business">Sport</option>
  </select><br/>
  <label for="category">Oznacz jako:</label>
  <select  class="category_input">
    <option value="draft">Szkic</option>
    <option value="ready">Gotowy</option>
    <option value="corrected">Poprawiony</option>
    <option value="approved">Zaakceptowany</option>
  </select><br/>
  <button class="save_button">Zapisz</button>
  <h1 class="title">{{ title }}</h1>
  <div id="editorContainer">
    <QuillEditor v-model:content="content" :options="options" toolbar='full' @text-change="updateContext" ref="quillEditor"  @ready="quillReady" content-type="html"/>
  </div>

<!--     Preview div - ql-editor class is needed to have styling as in the editor-->
    <p class = "section_title">PODGLĄD ARTYKUŁU</p>
  <h1 class="title">{{ title }}</h1>
    <div id="justText" v-html="content" class="content ql-editor"></div>

</template>


<style scoped>
 .section_title {
   font-size: 1.5rem;
   font-weight: 400;
   margin: 1rem 0;
 }

 #editorContainer,
 #justText {
   background-color: white;
   margin-bottom: 1rem;
 }

</style>