import {createRouter, createWebHistory} from "vue-router"
import Home from "../views/Home.vue"
import Article from "../components/MiniArticle.vue"
import Weather from "../views/Weather.vue"

// -artykuly i artykul- do usuniecia mozna przekierowac do 404 za pomoca useRouter np gdy dane zapytanie nie na wynikow 
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: 'home',
            component: Home
        },
        {
            path:"/pogoda",
            name:'weather',
            component: Weather
        },
        {
            path: "/:loc?-artykuly",
            component: Home,
        },
        {
            path: "/:loc?-artykuly/artykul?:subloc",
            component:Article, 
        },
        {
            path: '/404', 
            name: 'NotFound',
            component: {
                template: '<p>Page Not Found</p>'
            }
        },
        { 
            path: '/:pathMatch(.*)*', 
            name: 'DoesntExist',
            redirect: '404'
        }
    ]
})

export default router