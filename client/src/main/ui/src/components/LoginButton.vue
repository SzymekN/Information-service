<script setup>
defineProps({
  msg: {
    type: String,
    required: true
  }
})
</script>

<template>
  <!--TODO: multiple props to use msg and href in one or delete component and implement in Home.vue-->
  <div class="buttons">
    <button><a href="#">{{ msg }}</a></button>
  </div>
</template>

<style scoped>
  .buttons {
    float: right;
  }

  .buttons button {
    background-color: #ebf3fa;
    border: none;
    padding: 0.5rem;
    font-size: 0.8rem;

}

  .buttons:hover button {
    background-color: #d6e7ef;
}

</style>