import {createRouter, createWebHistory} from "vue-router"
import HomeView from "@/views/HomeView.vue"
import UserPanelView from "@/views/UserPanelView.vue"
import LoginView from "@/views/LoginView.vue"
import TheMainContent from "@/components/sub-pages/TheMainContent.vue"
import Business from "@/components/sub-pages/Business.vue"
import Article from "@/components/articles/Article.vue"
import LeagueTop5 from "@/components/api-leagues/LeagueTop5.vue"
import LeagueTable from "@/components/api-leagues/LeagueTable.vue"
import UserInfo from "@/components/user-layout/UserInfo.vue"
import Proposals from "@/components/user-layout/Proposals.vue"
import DraftsList from "@/components/user-layout/DraftsList.vue"
import Editor from "@/components/user-layout/Editor.vue"
import RegisterView from "@/views/RegisterView.vue";
import Manage from "@/components/user-layout/Manage.vue";

import jsCookie from 'js-cookie';
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
                    path: '/sport',
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
        },
        {
            path: "/:loc?-artykuly",
            component: HomeView,
        },
        {
            path: "/userpanel",
            name: 'userpanel',
            redirect: "/userpanel/profile",
            component: UserPanelView,
            children: [
                {
                    path: '/userpanel/profile',
                    name: 'profile',
                    component: UserInfo
                },
                {
                    path: "/userpanel/edit",
                    name: 'edit',
                    component: Editor
                },
                {
                    path: "/userpanel/proposals",
                    name: 'proposals',
                    component: Proposals
                },
                {
                    path: "/userpanel/articles",
                    name: 'draftsList',
                    component: DraftsList
                },
                {
                    path: "/userpanel/manage",
                    name: 'manageUsers',
                    component: Manage
                },
            ],
        },
        {
            path: "/login",
            component: LoginView,
        },
        {
            path: "/register",
            component: RegisterView,
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

//route guard
const protectedRoutes = ['userpanel', 'edit', 'proposals', 'draftsList', 'profile'];

const roleRoutes = {
    'ROLE_JOURNALIST': ['userpanel', 'edit', 'proposals', 'draftsList', 'profile'],
    'ROLE_CORRECTOR': ['userpanel', 'edit', 'draftsList', 'profile'],
    'ROLE_REDACTOR': ['userpanel', 'edit', 'proposals', 'draftsList', 'profile'],
    'ROLE_USER': ['userpanel', 'profile'],
    'ROLE_ADMIN': 'all'
}

router.beforeEach((to, from, next) => {
    let role = null;
    if (jsCookie.get('ROLE'))
        role = atob(jsCookie.get('ROLE'));

    if (!protectedRoutes.includes(to.name)) {
        next();
        return;
    }

    if (role && protectedRoutes.includes(to.name)){
        if (roleRoutes[role].includes(to.name) || role === 'ROLE_ADMIN') next();
        else next('/login')
    } else
        next('/')

})

export default router
