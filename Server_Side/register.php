<?php
include('config.php');
$NameApp=$_POST["userName"];
$PassApp=$_POST["password"];

$sql="SELECT COUNT(Name) as UNIVIDEO FROM Users WHERE Name like '" . mysqli_real_escape_string($conn, $NameApp ) . "'";

$result=$conn->query($sql);
while($row = $result->fetch_assoc()){
$nameFound=$row['UNIVIDEO'];
}

if($nameFound==0){
	
	$passCreate=$NameApp . $PassApp;
	$passCreate=md5($passCreate);
$sql="INSERT INTO Users (Name,Password,Overall_Score,Win,Lost) VALUES ('" . $NameApp . "' , '" . $passCreate . "',0,0,0)";
$result=$conn->query($sql);

//echo the ID of the user.
$sql="SELECT ID FROM Users WHERE Name like '" . mysqli_real_escape_string($conn, $NameApp ) . "'";
$result=$conn->query($sql);
while($row = $result->fetch_assoc()){
echo $row['ID'];
}


	
}else{
	
	echo 0;
	
	
}







?>

