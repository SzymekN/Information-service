<template>
  <div class="properties">   
    <div class="input-look">
      <div class="select-wrapper">
        <select  v-model="selectedOption"  @change="handleOptionChange">
          <option v-for="(optionName, optionKey) in optionNameMap" :value="optionKey" :key="optionKey">
            {{ optionName }}
          </option>
        </select>
      </div>
      <div class="input-wrapper">
        <input id="search-input" type="text" v-model="textInput" placeholder="Szukaj" class="disabled"> 
      </div>
      <div class="searchButton-wrapper">
        <button id="searchButton" @click="handleSearch" class="disabled">
          <img :src="SearchImage" alt="Wyszukaj" class="search-icon" />
        </button>
      </div>
      <div class="select-wrapper role-wrapper">
        <select v-model="selectedRole" @change="handleRoleChange">
          <option  v-for="(roleName, roleKey) in authorityNameMap" :value="roleKey" :key="roleKey">
            {{ roleName }}
          </option>
        </select>
      </div>
      <div class="user-add">
        <button id="addButton" @click="activateAddModal"><img :src="AddImage" alt="Dodaj użytkownika" class="add-icon" /></button>
      </div>
    </div>
  </div>
  <div class="table-context">
    <table-lite
        :is-static-mode="false"
        :columns="table.columns"
        :rows="table.rows"
        :total="table.totalRecordCount"
        :sortable="table.sortable"
        @is-finished="tableLoadingFinish"
        @row-clicked="tableLoadingFinish"
    ></table-lite>
  </div>
  <UserEdit v-if="editModalOpen" :user="selectedUser" @submit="updateUser" @close="closeModal('editModalOpen')"></UserEdit>
  <UserAdd v-if="addModalOpen" @submit="addUser" @close="closeModal('addModalOpen')"></UserAdd>
  <UserDelete v-if="deleteModalOpen" :user="selectedUser.username" :isAdmin="true" @submit="fetchDeleteUser" @close="closeModal('deleteModalOpen')"></UserDelete>
</template>


<script setup>
import jsCookie from "js-cookie";
import { reactive, ref, computed } from "vue";
import { toast } from "vue-sonner";
import TableLite from 'vue3-table-lite'
import EditImage from "@/assets/icons8-edit.svg";
import DeleteImage from "@/assets/icons8-trash.svg";
import SearchImage from "@/assets/icons8-search.svg";
import AddImage from "@/assets/icons8-add-user.svg";
import UserEdit from '@/components/modals/UserEdit.vue';
import UserAdd from "@/components/modals/UserAdd.vue";
import UserDelete from "@/components/modals/UserDelete.vue";

const editModalOpen = ref(false);
const addModalOpen = ref(false);
const deleteModalOpen = ref(false);
const selectedOption = ref("default");
const data = reactive([]);
var textInput = ref("");
var selectedUser = ref("null");
var url = 'null';
var page = 0;
var size = 10;
const rowCount = ref(0);

const selectedRole = ref("DEFAULT");
const authorityNameMap = {
  DEFAULT: 'Wszystkie role',
  ADMIN: 'Administrator',
  JOURNALIST: 'Dziennikarz',
  USER: 'Użytkownik',
  REDACTOR: 'Redaktor',
};

const optionNameMap = {
  default: '--Wybierz opcję--',
  username: 'Username',
  name: 'Imię',
  surname: 'Nazwisko',
  email: 'Email',
};

const fetchUsers = async () =>{
  url='/editorial/actions/get/users?';
  const role=selectedRole.value;
  const field=selectedOption.value;
  const value= textInput;

  // cleaning data before updating it with edited data  
  data.splice(0);

  // var tempUrl=url;
  url+=`page=${page}&size=${size}`;
  const queryParams = [];

  if (role&&role!=='DEFAULT') {
    queryParams.push(`role=${encodeURIComponent(role)}`);
  }

  if (field&&field!=='default' && value) {
    queryParams.push(`attributeName=${encodeURIComponent(field)}`);
    queryParams.push(`attributeValue=${encodeURIComponent(value)}`);
  }

  url +='&' + queryParams.join('&');

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if(response.status===204){
      // cleaning data when search result is empty
      data.splice(0, data.length);
    }
    else if (!response.ok) {
      const text = await response.text();
      toast.error(text)
    }
    else {
       const responseJson = await response.json();
      for (let i = 0; i < responseJson.length; i++) {
        if(responseJson[i]["id"]!==JSON.parse(sessionStorage.getItem('user')).id){
          data.push({
            id: responseJson[i]["id"],
            username: responseJson[i]["username"],
            name: responseJson[i]["name"],
            surname: responseJson[i]["surname"],
            email: responseJson[i]["email"],
            authorityName: responseJson[i]["authorityName"],
            supplier: responseJson[i]["supplier"],
          });
        }
      }
      rowCount.value = Number(response.headers.get('X-Total-Count'));
    }
  } catch (error) {
    console.log(error);
  }
}

fetchUsers();

// Table config
const table = reactive({
  columns: [
      {
          label: "Id",
          field: "id",
          width: "1%",
          sortable: true,
      },
      {
          label: "Username",
          field: "username",
          width: "1%",
          sortable: true,
      },
      {
          label: "Imie",
          field: "name",
          width: "5%",
          sortable: true,
      },
      {
          label: "Nazwisko",
          field: "surname",
          width: "1%",
          sortable: true,
      },
      {
          label: "Email",
          field: "email",
          width: "1%",
          sortable: true,
      },
      {
          label: "Rola",
          field: "authorityName",
          width: "1%",
          sortable: true,
      },
      {
          label: "Actions",
          field: "actions",
          width: "1%",
          sortable: false,
          display: function (row) {
              return `
              <button class="editButton" rowId=${row.id}>
                <img src="${EditImage}" alt="Edit" />
      </button>
      <button class="deleteButton" rowId=${row.id}>
        <img src="${DeleteImage}" alt="Delete" />
      </button>
  `;
          },
      },
  ],
  rows: computed(() => {
    console.log(data)
      return data
  }),
  totalRecordCount: computed(() => {
      return rowCount.value;
  }),
  sortable: {
      order: "id",
      sort: "asc",
  },
});

function addListeners(className, listenerFunction) {
  let elements = document.getElementsByClassName(className);

  Array.prototype.forEach.call(elements, function (element) {
    //checking if cell already has onclick assigned
    if (element.getAttribute('listener') !== 'true') {
      element.setAttribute('listener', 'true');
      element.addEventListener("click", listenerFunction);
    }
  });
}

const tableLoadingFinish = () => {
  table.isLoading = false;
  
  addListeners("deleteButton", activateDeleteModal);
  addListeners("editButton", activateEditModal);
};

const handleRoleChange = (e) => {
  selectedRole.value = e.target.options[e.target.options.selectedIndex].value;
  fetchUsers();
};
const handleOptionChange = (e) => {
  const inputElement = document.getElementById("search-input");
  const searchElement=document.getElementById("searchButton");

  if (e.target.options.selectedIndex == 0) {
    searchElement.classList.add("disabled");
    inputElement.classList.add("disabled");

    // Clearing search input
    inputElement.value="";
    // Clearing connected variable 
    textInput="";

  } else {
    searchElement.classList.remove("disabled");
    inputElement.classList.remove("disabled");
    selectedOption.value = e.target.options[e.target.options.selectedIndex].value;
  }
};

const handleSearch = () => {
  const inputElement = document.getElementById("search-input");
  // const searchElement=document.getElementById("searchButton");

  if (inputElement.classList.contains("disabled")) {
    return;
  }

  textInput = inputElement.value;
   fetchUsers();
  };
const activateDeleteModal = (pointerEvent) => {
  const rowId = parseInt(pointerEvent.currentTarget.getAttribute("rowId"));
  const row = data.find((item) => item.id === rowId);
  selectedUser = JSON.parse(JSON.stringify(row));
  deleteModalOpen.value = true;
};

function fetchDeleteUser() {
  url = `/editorial/actions/delete?id=${encodeURIComponent(selectedUser.id)}`;
  fetch(url, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      if (!response.ok) {
        const text = response.text();
        toast.error(text);
      } else {
        toast.success("Użytkownik został usunięty");
        deleteModalOpen.value = false;
        fetchUsers();
      }
    })
    .catch((error) => {
      console.log(error);
    });
}

const fetchEditUser = async (userData) =>{
  url='/editorial/actions/edit?';

  url +=`id=${encodeURIComponent(selectedUser.id)}`;
 
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
      console.log(text)
      toast.error(text)
    }
    else {
        editModalOpen.value = false;
        cleanModal('editModalOpen');
        // Fetch the updated list of users
        fetchUsers();
    }
  } catch (error) {
    console.log(error);
  }
}

const activateEditModal = (pointerEvent) => {
  const rowId = parseInt(pointerEvent.currentTarget.getAttribute("rowId"));
  const row = data.find((item) => item.id === rowId);
  selectedUser = JSON.parse(JSON.stringify(row));
  editModalOpen.value = true;
};

const getKeyByValue = (object, value) => {
  return Object.keys(object).find(key => object[key] === value);
};

function updateUser(userData) {
  const form = userData.currentTarget; // Select the form element
  const formFields = form.querySelectorAll('input, select');
  const fieldValues = {};

  // Get the values from the form fields
  formFields.forEach((field) => {
    const fieldName = field.id;
    const fieldValue = field.value;
    fieldValues[fieldName] = fieldValue;
  });

  const userToUpdate = data.find((user) => user.id === selectedUser.id);

  if (userToUpdate) {
    const updatedValues = {}; // Object to store the updated keys and values

    // Update the user object with the new values
    for (const [key, value] of Object.entries(fieldValues)) {
      if (value!="") { //userToUpdate[key] !== value&&
        updatedValues[key] = value; // Store the updated key-value pair
      }
      if(key=="authorityName"){
        updatedValues[key] = getKeyByValue(authorityNameMap,value);
      }
    }

    // console.log("Updated values:", updatedValues);
    fetchEditUser(updatedValues);
  } else {
    console.log("User not found");
  }
}

const cleanModal = (modal) => {
  const usernameInput = document.getElementById('username');
  const nameInput = document.getElementById('name');
  const surnameInput = document.getElementById('surname');
  var passwordInput;
  const authorityNameSelect = document.getElementById('authorityName');
// console.log(modal)
  if(modal==="addModalOpen"){
    passwordInput = document.getElementById('password');
    const emailInput = document.getElementById('email');
    emailInput.value = '';
  }
  else
    passwordInput = document.getElementById('password');
    // console.log(passwordInput);
     // Clear input field values
     usernameInput.value = '';
    nameInput.value = '';
    surnameInput.value = '';
    if(passwordInput!=null)
      passwordInput.value = '';
    // Reset select field to the default option
    authorityNameSelect.value = 'DEFAULT';
};

const closeModal = (modal) => {
  if(modal==="editModalOpen")
    editModalOpen.value = false;
  else if(modal==="addModalOpen"){
    cleanModal(modal);
    addModalOpen.value = false;
  }
  else if(modal==="deleteModalOpen"){
    // cleanModal(modal);
    deleteModalOpen.value = false;
  }
};

const activateAddModal = () => {
  addModalOpen.value = true;
};

const fetchAddUser = async (newUser) =>{
  url='/editorial/registration';
 
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newUser),
    });

    if (!response.ok) {
      const text = await response.text();
      toast.error(text)
    }
    else {
        // Clean and close the modal
        cleanModal('addModalOpen');
        addModalOpen.value = false;

        // Fetch the updated list of users
        fetchUsers();
    }
  } catch (error) {
    console.log(error);
  }
}

function addUser(userData) {
  
  const form = userData.currentTarget; // Select the form element

  const formFields = form.querySelectorAll('input, select');
  const fieldValues = {};

  // Get the values from the form fields
  formFields.forEach((field) => {
    const fieldName = field.id;
    const fieldValue = field.value;
    fieldValues[fieldName] = fieldValue;
  });
  

  // Check if all values are present
  for (const key in fieldValues) {
    if (!fieldValues[key]) {
      setTimeout(() => toast.error(`Missing value for ${key}`), 10)
    }
  }

  // Check if authorityName is present
  if (!fieldValues.authorityName||fieldValues.authorityName=="DEFAULT") {
    console.log("Missing value for authorityName.");
    return;
  }

  
  const newUser = {
    username: fieldValues.username,
    name: fieldValues.name,
    surname: fieldValues.surname,
    email: fieldValues.email,
    password: fieldValues.password,
    authorityName: fieldValues.authorityName
  };

  fetchAddUser(newUser);
};
</script>

<style scoped>
@import '../../assets/userLists.css';

.disabled{
  pointer-events: none;
  opacity: 0.4;
}
.properties {
  display: flex;
}

.input-look {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  width: 100%;
}

.select-wrapper,
.input-wrapper,
.searchButton-wrapper {
  flex: 1;
}
.role-wrapper{
  margin-left:5%;
}
.user-add{
  margin-left:5%;
  margin-right: 30%;
}
.select-wrapper{
  max-width:15%;
}
.input-wrapper{
  max-width:20%;
}
.searchButton-wrapper {
  max-width: 4%;
}
.select-wrapper{
  padding-top:2px;
}
.select-wrapper select,
.input-wrapper input {
  width: 100%;
  border: solid 1px;
  padding: 8px;
  border-radius: 4px;
}

#searchButton,
#addButton{
  height:40px;
  width:40px;
}
.add-icon{
  height:30px;
  width:30px;
  margin-left:2px;
  margin-top:2px;
}
</style>
