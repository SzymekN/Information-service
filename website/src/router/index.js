import {createRouter, createWebHistory} from "vue-router"
import HomeView from "../views/HomeView.vue"
import Article from "@/components/articles/Article.vue"
import Editor from "@/components/user-layout/Editor.vue"
import UserInfo from "@/components/user-layout/UserInfo.vue"
import Topics from "@/components/user-layout/Topics.vue"
import ArticlesList from "@/components/user-layout/ArticlesList.vue"
import Business from "@/components/sub-pages/Business.vue";
import UserPanelView from "@/views/UserPanelView.vue";
import TheMainContent from "@/components/sub-pages/TheMainContent.vue";
import LoginView from "@/views/LoginView.vue";

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
                }
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
        {
            path: "/:loc?-artykuly",
            component: HomeView,
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
                    name: 'edit',
                    component: Editor
                },
                {
                    path: "/userpanel/topics",
                    component: Topics
                },
                {
                    path: "/userpanel/articles",
                    component: ArticlesList
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
