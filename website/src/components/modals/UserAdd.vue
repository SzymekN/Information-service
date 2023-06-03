<template>
    <div class="user-modal">
        <div class="modal-content">
        <div class="heading">
            <h2>Dodaj użytkownika</h2>
            <div class="close-button" @click="$emit('close')">X</div>
        </div>
        <form @submit.prevent="submitForm">
            <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username">
            <label for="name">Imie:</label>
            <input type="text" id="name">
            <label for="surname">Nazwisko:</label>
            <input type="text" id="surname">
            <label for="email">Email:</label>
            <input type="text" id="email">
            <label for="password">Hasło:</label>
            <input type="password" id="password" placeholder="••••••••">
            </div>
            <div class="form-group">
            <label for="authorityName">Rola:</label>
            <select v-model="defaultRole" id="authorityName">
                <option v-for="(roleName, roleKey) in authorityNameMap" :value="roleKey" :key="roleKey">
                {{ roleName }}
                </option>
            </select>
            </div>
            <button type="submit">Dodaj</button>
        </form>
        </div>
    </div>
</template>

<script setup>
    const defaultRole = 'DEFAULT';
    const defaultVmodel='';
    const authorityNameMap = {
        DEFAULT: '--Wybierz rolę--',
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