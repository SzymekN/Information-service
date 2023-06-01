<template>
<div class="user-info">
  <h1>
    Twoje dane
  </h1>

  <span class="data-label">Imię: </span>
  <span></span>
  <br/>

  <span class="data-label">Nazwisko: </span>
  <span></span>
  <br/>

  <span class="data-label">Login: </span>
  <span></span>
  <br/>

  <span class="data-label">E-mail: </span>
  <span></span>
  <br/>

  <!-- <span class="data-label">Rola: </span>
  <span v-html="jsCookie.get('role')"></span>
  <br/> -->

  <button id="editButton" @click="activateModal">
    Edytuj profil
  </button>

  <button id="deleteButton" class="button-del" @click="activateModal">
    Usuń profil
  </button>
</div>
<UserEdit v-if="editModalOpen" :user="user" @submit="updateUser" @close="closeModal('editModalOpen')"></UserEdit>
<UserDelete v-if="deleteModalOpen" :user="user.id" @submit="deleteUser" @close="closeModal('deleteModalOpen')"></UserDelete>
</template>

<script setup>
    import jsCookie from 'js-cookie';
    import { ref } from 'vue';
    import UserDelete from '@/components/modals/UserDelete.vue';
    import UserEdit from '@/components/modals/UserEdit.vue';
    const editModalOpen = ref(false);
    const deleteModalOpen = ref(false);
    var user=ref({
      id: jsCookie.get('id'),
      username: jsCookie.get('username'),
      name: jsCookie.get('name'),
      surname: jsCookie.get('surname'),
      email: jsCookie.get('email'),
      supplier: jsCookie.get('supplier'),
      isAdmin: jsCookie.get('isAdmin')
    })
    const activateModal = (e) => {
      if (e.target.id === 'editButton') {
        editModalOpen.value = true;
      } else if (e.target.id === 'deleteButton') {
        deleteModalOpen.value = true;
      }
    }

    const closeModal = (modal) => {
      if (modal === 'editModalOpen') {
        editModalOpen.value = false;
      }
      else if(modal === 'deleteModalOpen'){
        deleteModalOpen.value = false;
      }
    }

    const updateUser=()=>{
      console.log('updateUser')
    }
    const deleteUser=()=>{
      console.log('deleteUser')
    }

    const cleanModal=(modal)=>{
      const usernameInput = document.getElementById('username');
      const nameInput = document.getElementById('name');
      const surnameInput = document.getElementById('surname');
      const emailInput = document.getElementById('email')||'';
      const passwordInput = document.getElementById('password')||'';
      const passwordCheckInput = document.getElementById('passwordCheck')||'';

      usernameInput.value = '';
      nameInput.value = '';
      surnameInput.value = '';
      if(emailInput){
        emailInput.value = '';
      }
      if(passwordInput){
        passwordInput.value = '';
      }
      if(passwordCheckInput){
        passwordCheckInput.value = '';
      }
  }
</script>

<style>
span {
  margin: 3% 1%;
}

.user-info {
  margin-top: 2rem;
  background: white;
  width: fit-content;
  padding: 3%;
  border-radius: 10px;
  min-width: 60%;
  box-shadow: 1px 1px 20px -10px;
  margin-left: 20%;
}

.data-label {
  font-weight: 550;
}

.user-info button {
  margin: 5% 2% 2% 2%;
  padding: 1%;
  background: rgb(238, 246, 253);
  border: none;
  width: fit-content;
  font-size: 0.9rem;
  cursor: pointer;
  /*border-radius: 10px;*/
}

.user-info button:hover {
  background-color: #c8d8f1;
}

.user-info .button-del:hover {
  background-color: #ef4e70;
}
</style>