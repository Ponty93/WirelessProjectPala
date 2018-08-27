<?php

  
  
include('config.php');
$nameApp="0";
$PassApp="0";
$NameApp=$_POST["tableName"];
$PassApp=$_POST["attrToQuery"];






$sql="SELECT ID,Password,Name FROM Users WHERE Name like '" . mysqli_real_escape_string($conn, $NameApp ) . "'";

$result=$conn->query($sql);

while($row = $result->fetch_assoc()){
$MD5DB=$row['Password'];
$id_user=$row['ID'];
$user_name=$row['Name'];
}
//$unione=$NameApp . $PassApp;
//$MD5APP=md5($unione);
if($PassApp==$MD5DB){   // $MD5DB uses infos from the official database
$login=1;






}else{ $login=0;}


$parameters = array('Result' => $login,'playerId' => $id_user, 'userName' => $user_name);
header('Content-Type: application/json');
echo json_encode($parameters, TRUE);
?>