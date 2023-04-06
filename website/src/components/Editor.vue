<script setup>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import '@vueup/vue-quill/dist/vue-quill.core.css';
import { ref, reactive } from 'vue';

const quillEditor = ref()
const content = ref("")
const options = reactive({
    theme: 'snow',
    placeholder: 'Zacznij pisać artykuł...',
})


// TODO: 
// - autosave every n minutes/seconds,
// - button to add gallery to the article
function updateContext(){

  var text = quillEditor.value.getHTML();
  console.log(text)

}

function quillReady(){
  console.log("quill ready")
}

</script>

<template>

    <div id="editorContainer">
      <QuillEditor v-model:content="content" :options="options" toolbar='full' @text-change="updateContext" ref="quillEditor"  @ready="quillReady" content-type="html"/>
      
    </div>

<!--     Preview div - ql-editor class is needed to have styling as in the editor-->
    <p class = "section_title">PODGLĄD ARTYKUŁU</p>
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