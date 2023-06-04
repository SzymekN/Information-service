<template>
  <div v-if="user" class="user-info">
    <h1>
      Twoje dane
    </h1>
  
    <span class="data-label">Imię: </span>
    <span>{{ user.name }}</span>
    <br/>
  
    <span class="data-label">Nazwisko: </span>
    <span>{{ user.surname }}</span>
    <br/>
  
    <span class="data-label">Login: </span>
    <span>{{ user.username }}</span>
    <br/>
  
    <span class="data-label">E-mail: </span>
    <span>{{ user.email }}</span>
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
  <UserDelete v-if="deleteModalOpen" :user="JSON.stringify(user.id)" @submit="fetchDeleteUser" @close="closeModal('deleteModalOpen')"></UserDelete>
  </template>
  
  <script setup>
      import jsCookie from 'js-cookie';
      import { ref } from 'vue';
      import UserDelete from '@/components/modals/UserDelete.vue';
      import UserEdit from '@/components/modals/UserEdit.vue';
      import { logout } from '@/scripts/Scripts.ts';
import { toast } from 'vue-sonner';
      const editModalOpen = ref(false);
      const deleteModalOpen = ref(false);
  
      var user=ref();
      // console.log(user)
      var url;
  
      const getUser= async ()=>{
          try{
            const response = await fetch('/editorial/actions/user/info', {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
              },
            }).catch(error=>{if(error.reponse.status===401){
              toast.error("Nie potwierdzono linka w mailu.")
            }
          });
            if (!response.ok) {
              const text = await response.text();
              console.log(text)
            }
            else {
              const responseJson = await response.json();
              user.value = responseJson;
              sessionStorage.setItem("user", JSON.stringify(user));        
              return responseJson;
            }
          }
          
          catch(error){
            console.log(error);
          }
      };
  
      getUser();
  
      var url;
  
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
  
      const fetchEditUser = async (userData) =>{
      url='/editorial/actions/edit?';
      console.log(userData)
      url +=`id=${encodeURIComponent(JSON.stringify(user.value.id))}`;
      try {
        const response = await fetch(url, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(userData),
        });
  
        if (!response.ok) {
          const text = await response.text();
          toast.error(text)
          console.log(text)
        }
        else {
            editModalOpen.value = false;
            cleanModal();
            alert("Zmieniono dane użytkownika. Zaloguj się ponownie.")
            jsCookie.remove('role');
            jsCookie.remove('ROLE');
            window.location.href = '/home';          
        }
      } catch (error) {
        console.log(error);
      }
  }
  
      const updateUser=(userData)=>{
        const form=userData.currentTarget;
        const formFields = form.querySelectorAll('input, select');
        const fieldValues = {};
  
        // Get the values from the form fields
        formFields.forEach((field) => {
          const fieldName = field.id;
          const fieldValue = field.value;
          fieldValues[fieldName] = fieldValue;
        });
        console.log(atob(jsCookie.get("ROLE")))
        fieldValues['authorityName']=atob(jsCookie.get("ROLE")).substring(5);
  
        const updatedValues = {}; // Object to store the updated keys and values
  
      // Update the user object with the new values
      for (const [key, value] of Object.entries(fieldValues)) {
        if (value!="") { //userToUpdate[key] !== value&&
          updatedValues[key] = value; // Store the updated key-value pair
        }
      }
      fetchEditUser(updatedValues);
      }
  
      function fetchDeleteUser() {
    url = `/editorial/actions/delete?id=${encodeURIComponent(JSON.stringify(user.value.id))}`;
    fetch(url, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => {
        if (!response.ok) {
          const text = response.text();
          console.log(text)
          // toast.error(text);
        } else {
          alert("Pomyślnie usunięto konto")
          deleteModalOpen.value = false;
          logout();
        }
      })
      .catch((error) => {
        console.log(error);
      });
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