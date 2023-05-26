<template>
  <div class="properties">   
    <div class="input-look">
      <div class="select-wrapper">
        <select ref="selectedOption" @change="handleOptionChange">
          <option value="default" selected>--Wybierz opcję--</option>
          <option value="username">Username</option>
          <option value="name">Imie</option>
          <option value="surname">Nazwisko</option>
          <option value="email">Email</option>
        </select>
      </div>
      <div class="input-wrapper">
        <input id="search-input" type="text" v-model="textInput" placeholder="Szukaj" class="disabled"> 
      </div>
      <div class="search-button-wrapper">
        <button id="search-button" @click="submit" class="disabled">
          <img :src="SearchImage" alt="Wyszukaj" class="search-icon" />
        </button>
      </div>
      <div class="select-wrapper role-wrapper">
        <select v-model="selectedRole">
          <option id="role-sefetchUserslect" v-for="(roleName, roleKey) in authorityNameMap" :value="roleKey" :key="roleKey">
            {{ roleName }}
          </option>
        </select>
      </div>
      <div class="user-add">
        <button id="add-button" @click="addTopic"><img :src="AddImage" alt="Dodaj użytkownika" class="add-icon" /></button>
      </div>
    </div>
  </div>
  <div class="table-context">
    <table-lite
        :is-static-mode="true"
        :columns="table.columns"
        :rows="table.rows"
        :total="table.totalRecordCount"
        :sortable="table.sortable"
        @is-finished="tableLoadingFinish"
        @row-clicked="tableLoadingFinish"
    ></table-lite>
  </div>
  <UserEdit v-if="isUserModalOpen" :user="selectedUser" @submit="updateUser" @close="closeModal"></UserEdit>
</template>


<script setup>
// to do after changing role value in search box appears back in place?
import jsCookie from "js-cookie";
import { reactive, ref, computed } from "vue";
import { toast } from "vue-sonner";
import TableLite from 'vue3-table-lite'
import EditImage from "@/assets/icons8-edit.svg";
import DeleteImage from "@/assets/icons8-trash.svg";
import SearchImage from "@/assets/icons8-search.svg";
import AddImage from "@/assets/icons8-add-user.svg";
import UserEdit from '@/components/modals/UserEdit.vue';

const isUserModalOpen = ref(false);
const selectedOption = ref("");
const textInput = ref("");
const data = reactive([]);
var selectedUser = ref("null");
var url = '/editorial/actions/get/users?';
var page = 0;
var size = 10;
const selectedRole = ref("DEFAULT");

const authorityNameMap = {
  DEFAULT: '-- Wybierz rolę --',
  ADMIN: 'Administrator',
  JOURNALIST: 'Dziennikarz',
  USER: 'Użytkownik',
  REDACTOR: 'Redaktor',
};

const searchTerm = ref(""); // Search text
const newTopicProposal = ref(""); // user input with proposition
const maxId = ref(128); // last id assigned

const fetchUsers = async (role, field, value) =>{
  url+=`page=${page}&size=${size}`;
  const queryParams = [];

  if (role) {
    queryParams.push(`role=${encodeURIComponent(role)}`);
  }

  if (field && value) {
    queryParams.push(`field=${encodeURIComponent(field)}`);
    queryParams.push(`value=${encodeURIComponent(value)}`);
  }

  url += queryParams.join('&');

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      const text = await response.text();
      toast.error(text)
    }
    else {
      console.log(response)
       const responseJson = await response.json();
       console.log(responseJson)
      for (let i = 0; i < responseJson.length; i++) {
         data.push({
          id: responseJson[i]["id"],
          username: responseJson[i]["username"],
          name: responseJson[i]["name"],
          surname: responseJson[i]["surname"],
          email: responseJson[i]["email"],
          authorityName: responseJson[i]["authorityName"],
         });
       }
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
      return data
       .filter((x) => x.authorityName = authorityNameMap[x.authorityName])
  }),
  totalRecordCount: computed(() => {
      return table.rows.length;
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
  // addListeners("topic", changeTopicListener);
  if (jsCookie.get('role') == 'admin' || jsCookie.get('role') == 'redactor')
      addListeners("state", changeStateListener);
  
    addListeners("deleteButton", deleteUser);
    addListeners("editButton", editUser);
};

const handleOptionChange = (e) => {
  const inputElement = document.getElementById("search-input");
  const searchElement=document.getElementById("search-button");

  if (e.target.options.selectedIndex == 0) {
    searchElement.classList.add("disabled");
    inputElement.classList.add("disabled");
    inputElement.value="";
    // fetch for all data make if to make it not fetch data if it already present
  } else {
    searchElement.classList.remove("disabled");
    inputElement.classList.remove("disabled");
  }
};



function deleteUser() {
  let id=this.getAttribute("rowId");
  console.log("Delete data with id:", id);
}

const editUser = (pointerEvent) => {
  const rowId = parseInt(pointerEvent.currentTarget.getAttribute("rowId"));
  const row = data.find((item) => item.id === rowId);
  selectedUser = JSON.parse(JSON.stringify(row));
  console.log(selectedUser);
  isUserModalOpen.value = true;
};

function updateUser(userData) {
  isUserModalOpen.value = false;

  const form = userData.currentTarget; // Select the form element

  const formFields = form.querySelectorAll('input, select');
  const fieldValues = {};

  // Get the values from the form fields
  formFields.forEach((field) => {
    const fieldName = field.id;
    const fieldValue = field.value;
    fieldValues[fieldName] = fieldValue;
  });

  console.log(fieldValues);

  const userToUpdate = data.find((user) => user.id === selectedUser.id);

  if (userToUpdate) {
    const updatedValues = {}; // Object to store the updated keys and values

    // Update the user object with the new values
    for (const [key, value] of Object.entries(fieldValues)) {
      console.log(value+" "+userToUpdate[key]);
      if (userToUpdate[key] !== value&&value!="") {
        updatedValues[key] = value; // Store the updated key-value pair
      }
    }

    
    console.log("Updated values:", updatedValues);
  } else {
    console.log("User not found");
  }
  // TODO: Send a request to update the user on the server
}

const closeModal = () => {
  isUserModalOpen.value = false;
};

</script>

<style>
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
.search-button-wrapper {
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
.search-button-wrapper {
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

#search-button,
#add-button{
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
