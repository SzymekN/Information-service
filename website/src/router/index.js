import {createRouter, createWebHistory,useRoute} from "vue-router"
import HomeView from "../views/HomeView.vue"
import Article from "@/components/articles/Article.vue"
import EditView from "../views/EditView.vue"
import Editor from "@/components/user-layout/Editor.vue"
import UserInfo from "@/components/user-layout/UserInfo.vue"
import Business from "@/components/sub-pages/Business.vue"
import UserPanelView from "@/views/UserPanelView.vue"
import TheMainContent from "@/components/sub-pages/TheMainContent.vue"
import LoginView from "@/views/LoginView.vue"
import BuisnessView from "@/views/kategories/BuisnessView.vue"
import LeagueTop5 from "../components/api-leagues/LeagueTop5.vue"
import LeagueTable from "@/components/api-leagues/LeagueTable.vue"
import Topics from "@/components/user-layout/Topics.vue"


// -artykuly i artykul- do usuniecia mozna przekierowac do 404 za pomoca useRouter np gdy dane zapytanie nie na wynikow 
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: 'home',
            component: HomeView,
            redirect: "/home",
            children: [
                {
                    path: "/home",
                    component: TheMainContent
                },
                {
                    path: '/article',
                    component: Article
                },
                {
                    path: '/business',
                    component: Business
                },
                {
                    path: '/sport/',
                    name: 'leagueTop5',
                    component: LeagueTop5,
                    params: false,
                },
                
                {
                    path: '/sport/:league',
                    name: 'leagueTable',
                    component: LeagueTable,
                },
             
                
            ],
            component: HomeView,
            redirect: "/home",
            children: [
                {
                    path: "/home",
                    component: TheMainContent
                },
                {
                    path: '/article',
                    component: Article
                },
                {
                    path: '/business',
                    component: Business
                }
            ],
        },
        // TODO: DEPRECIATED - DELETE AFTER REFACTOR
        {
            path: "/biznes",
            component: BuisnessView
        },
        {
            path: "/:loc?-artykuly",
            component: HomeView,
        },
        // TODO: DEPRECIATED - DELETE AFTER REFACTOR
        {
            path: "/edit",
            component: EditView,
        },
        {
            path: "/userpanel",
            name: 'userpanel',
            redirect: "/userpanel/info",
            component: UserPanelView,
            children: [
                {
                    path: '/userpanel/info',
                    component: UserInfo
                },
                {
                    path: "/userpanel/edit",
                    component: Editor
                },
                {
                    path: "/userpanel/topics",
                    component: Topics
                },
                {
                    path: "/userpanel/profile",
                    redirect: '/userpanel/info'
                },
            ],
        },
        {
            path: "/login",
            component: LoginView,
        },
        // {
        //     path: "/article/:subloc?",
        //     component: Article, 
        // },
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
