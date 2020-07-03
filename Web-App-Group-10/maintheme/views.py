from django.shortcuts import render
import requests
import json
#Create your views here.
r=None
p=None



def login(request) :
   if (request.method)=="POST":
       uname=request.POST.get('username') 
       pas=request.POST.get('password')
       rme=request.POST.get('customCheck')
       global r 
       global p
       try:
        r = requests.post('https://group-10-api.herokuapp.com/Adminlogin',data={'username':uname,'password':pas})
        p= r.json()['access_token']
        return render(request,'index.html')
       except KeyError:
        return render(request, "login.html")

   return render(request, "login.html")


def index(request) :
    return render(request,"index.html")
   

def add_sport(request) :
  if(p!=None):
    if (request.method)=="POST":
         Sport_id=request.POST.get('Sport_id') 
         Sport_name=request.POST.get('Sport_name') 
         cat=request.POST.get('Sport_category') 
         mini=request.POST.get('min_team_size') 
         maxi=request.POST.get('max_team_size') 
         gender=request.POST.get('gender') 

         print(Sport_id,mini)
         a = requests.post('https://group-10-api.herokuapp.com/sportdetails',headers={'Authorization':f'Bearer {p}'},data={'sport_id':Sport_id,'sport_name':Sport_name,'sport_category':cat,'mini_team_size':mini,'max_team_size':maxi,'gender':gender})
         print(a)
         return render(request,'add_sport.html')
    else:
        return render(request,'add_sport.html')
  else:
    return render(request,"login.html")


def edit_sport(request) :
  if(p!=None):
    if (request.method)=="POST":

         sport_name=request.POST.get('sport_name') 
         start_date=request.POST.get('start_date') 
         end_date=request.POST.get('end_date') 
         print(sport_name,start_date,end_date)
         a = requests.post('https://group-10-api.herokuapp.com/add_dates',headers={'Authorization':f'Bearer {p}'},data={'sport_name':sport_name,'start_date':start_date,'end_date':end_date})
        
         return render(request,'edit_sport.html')
    else:
        return render(request,'edit_sport.html')
  else:
    return render(request,"login.html")

    #return render(request, "edit_sport.html")

def view_sport(request) :
  if(p!=None):
    if (request.method)=="POST":
      #   Sport_id=request.POST.get('Sport_id') 
       #  Sport_name=request.POST.get('Sport_name') 
         
         sport_category=request.POST.get('sport_category')

         a = requests.get('https://group-10-api.herokuapp.com/sport_category',headers={'Authorization':f'Bearer {p}'},data={'sport_category':sport_category})
         sc = a.json()

         
         return render(request,'view_sport.html',{'sc':sc})
    else:
        return render(request,'view_sport.html',{'sc':"False"})
  else:
    return render(request,"login.html")

def add_schedule(request) :
  if(p!=None):
    if (request.method)=="POST":
        
         sport_name=request.POST.get('sport_name') 
         team1_id=request.POST.get('team1_id') 
         team2_id=request.POST.get('team2_id') 
         match_date=request.POST.get('match_date') 
         match_title=request.POST.get('match_title')
         start_time=request.POST.get('start_time') 
         reporting_time=request.POST.get('reporting_time') 
         team1=request.POST.get('team1') 
         team2=request.POST.get('team2') 
         venue=request.POST.get('venue') 


         a = requests.post('https://group-10-api.herokuapp.com/add_schedule',headers={'Authorization':f'Bearer {p}'},data={'sport_name':sport_name,'team1_id':team1_id,'team2_id':team2_id,'match_date':match_date,'match_title':match_title,'start_time':start_time,'reporting_time':reporting_time,'team1':team1,'team2':team2,'venue':venue})
         print(a)
         return render(request,'add_schedule.html')
    else:
        return render(request,'add_schedule.html')
  else:
    return render(request,"login.html")

def edit_schedule(request):
  if(p!=None):
    if (request.method)=="POST":
      #   Sport_id=request.POST.get('Sport_id') 
       #  Sport_name=request.POST.get('Sport_name') 
         team1_id=request.POST.get('team1_id') 
         team2_id=request.POST.get('team2_id') 
         match_date=request.POST.get('match_date') 
         match_title=request.POST.get('match_title')
         start_time=request.POST.get('start_time') 

         reporting_time=request.POST.get('reporting_time') 
         team1=request.POST.get('team1') 
         team2=request.POST.get('team2') 
         venue=request.POST.get('venue') 

         a = requests.post('https://group-10-api.herokuapp.com/Modify_schedule',headers={'Authorization':f'Bearer {p}'},data={'team1_id':team1_id,'team2_id':team2_id,'match_date':match_date,'match_title':match_title,'start_time':start_time,'reporting_time':reporting_time,'team1':team1,'team2':team2,'venue':venue})
         print(a)
         return render(request,'edit_schedule.html')
    else:
        return render(request,'edit_schedule.html')
  else:
    return render(request,"login.html")

def view_schedule(request):
  if(p!=None):
    if (request.method)=="POST":
      #   Sport_id=request.POST.get('Sport_id') 
       #  Sport_name=request.POST.get('Sport_name') 
         
         sport_name=request.POST.get('sport_name')

         a = requests.get('https://group-10-api.herokuapp.com/schedule',headers={'Authorization':f'Bearer {p}'},data={'sport_name':sport_name})
         sc = a.json()

         print(sc)
         return render(request,'view_schedule.html',{'sc':sc})
    else:
        return render(request,'view_schedule.html',{'sc':"False"})
  else:
    return render(request,"login.html")

  

def edit_registration(request):
  if(p!=None):
    if (request.method)=="POST":
      #   Sport_id=request.POST.get('Sport_id') 
       #  Sport_name=request.POST.get('Sport_name') 
         team_name=request.POST.get('team_name') 
         

         a = requests.post('https://group-10-api.herokuapp.com/team_details',headers={'Authorization':f'Bearer {p}'},data={'team_name':team_name})
         print(a)
         return render(request,'edit_registration.html')
    else:
        return render(request,'edit_registration.html')
  else:
    return render(request,"login.html")

def view_registration(request):
  if(p!=None):
    if (request.method)=="POST":
      #   Sport_id=request.POST.get('Sport_id') 
       #  Sport_name=request.POST.get('Sport_name') 
         
         sport_name=request.POST.get('sport_name')
         team_name=request.POST.get('team_name')


         a = requests.get('https://group-10-api.herokuapp.com/team_members',headers={'Authorization':f'Bearer {p}'},data={'sport_name':sport_name,'team_name':team_name})
         sc = a.json()

         print(sc)
         return render(request,'view_registration.html',{'sc':sc})
    else:
        return render(request,'view_registration.html',{'sc':"False"})
  else:
    return render(request,"login.html")

def add_registration(request):
  if(p!=None):
    if (request.method)=="POST":
      #   Sport_id=request.POST.get('Sport_id') 
       #  Sport_name=request.POST.get('Sport_name') 
         
         sport_name=request.POST.get('sport_name')
         #team_name=request.POST.get('team_name')


         a = requests.get('https://group-10-api.herokuapp.com/team_details',headers={'Authorization':f'Bearer {p}'},data={'sport_name':sport_name})
         sc = a.json()

         print(sc)
         return render(request,'add_registration.html',{'sc':sc})
    else:
        return render(request,'add_registration.html',{'sc':"False"})
  else:
    return render(request,"login.html")