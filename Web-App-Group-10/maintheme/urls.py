from django.urls import path

from . import views

urlpatterns = [

	path('',views.login, name="login"),
    path("index",views.index, name="index"),
    
    path("add_sport",views.add_sport, name="add_sport"),
    path("edit_sport",views.edit_sport, name="edit_sport"),
    path("view_sport",views.view_sport, name="view_sport"),
    path("add_schedule",views.add_schedule, name="add_schedule"),
    path("edit_schedule",views.edit_schedule, name="edit_schedule"),
    path("edit_registration",views.edit_registration, name="edit_registration"),
    path("view_registration",views.view_registration, name="view_registration"),
    path("view_schedule",views.view_schedule, name="view_schedule"),
    path("add_registration",views.add_registration, name="add_registration"),




    




    

]




    
