<script>
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';

export default {
  components: {
    QuillEditor
  }
}
</script>

<template>
    <div>
        <QuillEditor theme="snow" toolbar="full"/>
    </div>
</template>


<style scoped>
 div {
    width: 100%;
    height: 100vh;
 }
</style>
