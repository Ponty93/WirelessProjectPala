<?php
include('config.php');
$nameApp="0";
$PassApp="0";
$NameApp=$_GET["Name"];
$PassApp=$_GET["Pass"];
$sql="SELECT Password FROM Users WHERE Name like '" . mysqli_real_escape_string($conn, $NameApp ) . "'";

$result=$conn->query($sql);

while($row = $result->fetch_assoc()){
$MD5DB=$row['Password'];
}
$unione=$NameApp . $PassApp;
$MD5APP=md5($unione);
if($MD5APP==$MD5DB){   //MD5APP it's the MD5 string calculated from the infos the App gave , $MD5DB uses infos from the official database
echo '1';

}else{ echo '0';}

?>