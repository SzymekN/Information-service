<script setup>
import list from "@/data/userNav.json"
import jsCookie from "js-cookie";

const hasRole = (role) => {
  if (jsCookie.get("ROLE"))
    return role.includes(atob(jsCookie.get("ROLE")));
  else return false;
};


</script>

<template>
  <table>
    <div class="buttons" v-for="item in list">
      <button v-if="!item.authorities || hasRole(item.authorities)"><router-link :to="item.link" ><a>{{item.text}}</a></router-link></button>
    </div>
  </table>
</template>



<style scoped>
table {
  width: 100%;
}

.buttons {
  margin: 0.2rem;
  align-content: center;
}

.buttons button {
  background-color: rgba(240, 248, 255, 0.6);
  border: none;
  font-size: 0.9rem;
  width: 100%;
  border-radius: 10px;
}

.buttons button:hover {
  background-color: rgba(240, 248, 255);
  box-shadow: 1px 1px 20px -10px;
}

a {
  display: block;
  padding: 0.3rem;
}

</style>