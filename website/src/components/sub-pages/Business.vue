<script setup>
import Currencies from "@/components/api-currencies/Currencies.vue";
import ArticleSection from "@/components/articles/ArticleSection.vue";

</script>

<template>
    <Currencies/>
    <p class = "section_title">O TYM SIĘ MÓWI W BIZNESIE</p>
    <hr>
    <ArticleSection :category="'politics'"/>
</template>

<style scoped>

</style>