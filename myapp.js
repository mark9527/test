var userCtr=angular.module('userCtr',[])userCtr.constant('baseUrl','http://localhost:8888/user/')userCtr.controller("timeCtr",function($scope){ $scope.clock={now:new Date()} var updateTime=function(){  $scope.clock.now=new Date(); } setInterval(function(){$scope.$apply(updateTime)},1000)})userCtr.provider('userProvider',{ $get:function(){  return{   message: function(result){alert("code: "+result.code+" message: "+result.message);}  } }}) userCtr.controller('test',function($scope,$http,userProvider,baseUrl){ $http({  method:'GET',  url:baseUrl+'begin' }).then(function(result){  userProvider.index=result.data;  $scope.index=userProvider.index; })})
userCtr.controller('getallCtr',function($scope,$http,baseUrl){ $http({  method:'GET',  url:baseUrl+'getall' }).then(function(result){  $scope.result=result.data; })})
userCtr.controller('updateCtr',function($scope,$http,$routeParams,userProvider,baseUrl){  $http({  method:'GET',  url:baseUrl+'update/'+$routeParams.id }).then(function(response){  var temp=response.data;  $scope.result=temp;  userProvider.message(temp); },function(error){  var temp=error.data;  alert(temp.message); })})
userCtr.controller('updateUser',function($scope,$http,userProvider,baseUrl){ $scope.namere='^[a-z][a-zA-Z]{3,}[0-9]$'; $scope.agere='^[1-9]|([1-9]\d)$'; $scope.update=function(){  $http({   method:'POST',   url:baseUrl+'updateUser',   data:$scope.result.obj  }).then(function(response){   var temp=response.data;   $scope.result=temp;   userProvider.message(temp);  },function(error){   var temp=error.data;   alert(temp.message);  }) }})
userCtr.controller('insertCtr',function($scope,$http,userProvider,$parse,baseUrl){
 $scope.$watch("user.id",function(newVal,oldVal,scope){  if(newVal!=oldVal&&newVal!=""){   var parseFun=$parse(newVal);   $http({    method:'GET',    url:baseUrl+'isUnique/'+parseFun(scope)   }).then(function(response){    $scope.isUnique=response.data.obj;   },function(error){    $scope.isUnique=false;   })  } }) $scope.namere='^[a-z][a-zA-Z]{3,}[0-9]$'; $scope.agere='^[1-9][0-9]$'; $scope.insert=function(){  $http({   method:'POST',   url:baseUrl+'insert',   data:$scope.user  }).then(function(response){   var temp=response.data;   $scope.result=temp;   userProvider.message(temp);  },function(error){   var temp=error.data;   alert(temp.message);  }) } })userCtr.controller('deleteCtr',function($scope,$http,$routeParams,userProvider,baseUrl){ $http({  method:'GET',  url:baseUrl+'delete/'+$routeParams.id }).then(function(response){  var temp=response.data;  $scope.result=temp;  userProvider.message(temp); },function(error){  var temp=error.data;  alert(temp.message); })})
