<template>
  <router-view/>
</template>
<script setup>
import axios from "axios";
import { provide } from "vue";
provide("axios",axios);
</script>

