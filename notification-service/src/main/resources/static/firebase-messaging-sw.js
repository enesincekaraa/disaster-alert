importScripts('https://www.gstatic.com/firebasejs/10.0.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.0.0/firebase-messaging-compat.js');

firebase.initializeApp({
    apiKey: "AIzaSyBrJvtQjoIc6YCPxtQlDy_BpgAORvsLJLY",
    authDomain: "cloud-messaging-5dfae.firebaseapp.com",
    projectId: "cloud-messaging-5dfae",
    storageBucket: "cloud-messaging-5dfae.firebasestorage.app",
    messagingSenderId: "1035025005698",
    appId: "1:1035025005698:web:92163d430a88d40dde889b"
});

const messaging = firebase.messaging();