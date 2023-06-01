<template>
    <div class="user-modal">
      <div class="modal-content">
        <div class="heading">
          <h2>Edytuj użytkownika</h2>
          <div class="close-button" @click="$emit('close')">X</div>
        </div>
        <form @submit.prevent="submitForm">
          <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" v-model="userInfo.username" id="username">
            <label for="name">Imie:</label>
            <input type="text" v-model="userInfo.name" id="name">
            <label for="surname">Nazwisko:</label>
            <input type="text" v-model="userInfo.surname" id="surname">
            <!-- <label for="email">Email:</label>
            <input type="text" v-model="userInfo.email" id="email"> -->
            <label v-if="userInfo.supplier==='APP'" for="password">Hasło:</label>
            <input v-if="userInfo.supplier==='APP'" type="password" v-model="userInfo.password" id="passwordToChange" placeholder="••••••••">
          </div>
          <div v-if="userInfo.authorityName==='ADMIN'" class="form-group">
            <label for="authorityName">Rola:</label>
            <select v-model="userInfo.authorityName" id="authorityName">
                <option v-for="(roleName, roleKey) in authorityNameMap" :value="roleName" :key="roleKey">
                  {{ roleName }}
                </option>
              </select>
          </div>
          <button type="submit" >Zapisz</button>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { reactive  } from 'vue';

  const props = defineProps(['user']);
  const userInfo = reactive({
    username: props.user.username || '',
    name: props.user.name || '',
    surname: props.user.surname || '',
    password: '',
    // email: props.user.email || '',
    authorityName: props.user.authorityName || '',
    supplier: props.user.supplier || '',
    isAdmin: props.user.isAdmin || false
  });
  
  const authorityNameMap = {
    ADMIN: 'Administrator',
    JOURNALIST: 'Dziennikarz',
    USER: 'Użytkownik',
    REDACTOR: 'Redaktor',
  };
  </script>
  
  <style scoped>
  .user-modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
  }
  
  .modal-content {
    background-color: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    width: 30%;
  }
  
  .heading {
    margin-bottom: 20px;
    height: 50px;
  }
  
  .form-group {
    margin-bottom: 20px;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
  }
  
  input,
  select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  button {
    padding: 8px 16px;
    background-color: #4caf50;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    float:right;
  }
  .close-button {
    position: absolute;
    top: 5px;
    right: 5px;
    font-size: 18px;
    cursor: pointer;
    color: white;
    background-color: red;
    border-radius: 25%;
    width: 25px;
    height: 25px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .close-button:hover {
    background-color: darkred;
  }
  h2{
    margin:0;
  }
  </style>