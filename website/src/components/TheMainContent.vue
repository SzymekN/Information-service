<script setup>
import TopNews from "@/components/TopNews.vue";
import Article from "@/components/MiniArticle.vue"
</script>

<template>
  <div class="section_content">

    <p class = "section_title">O TYM SIĘ MÓWI</p>
    <hr>
    <TopNews/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>

  </div>

  <div class="section_content">

    <p class = "section_title">EDUKACJA</p>
    <hr>
    <TopNews/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>
    <Article/>

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