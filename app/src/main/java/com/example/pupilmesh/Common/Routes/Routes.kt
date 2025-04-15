package com.example.pupilmesh.Common.Routes



sealed class Routes (val routes:String){


    object LoginRoute:Routes("login")
    object SignUpRoute:Routes("signup")
    object HomeRoute:Routes("home")
    object MangaDetailRoute : Routes("manga")
}