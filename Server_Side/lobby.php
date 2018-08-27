<?php
include('config.php');

$playerID=$_POST["valToQuery"];
$userName=$_POST["attrToQuery"];
$op=$_POST['op'];
$tableName=$_POST['tableName'];

$partner=0;


//

	  
	  //
	  
	  
	  




/*controllo me stesso*/
$sql="SELECT COUNT(id_lobby) as eskerer,matched FROM Lobby WHERE ID like " . $playerID;
$risult=$conn->query($sql);
while($row = $risult->fetch_assoc()){
			$counter=$row['eskerer'];
			$immatched=$row['matched'];
		}
	
	
	
	if($counter == 0){
		$immatched=2;
	}
	
	
	//
	
	//check delete lobby...
	if(($op=="delete" || $tableName=="delete" )&& ($immatched==0 || $immatched==2) ){
			$sql3="DELETE FROM Lobby WHERE ID like " . $playerID ."";
			$result=$conn->query($sql3);
		$result=0;
		
	}else{
		
		

	
	
	

if($immatched==2){  //non ho una mia istanza di lobby quindi controllo se ci sono o meno perzone libbere
	/*controllo se non ci sono match libberi*/
$sql="SELECT * FROM Lobby WHERE ID !='" . $playerID . "' ORDER BY id_lobby ASC";
$risult=$conn->query($sql);
while($row = $risult->fetch_assoc()){
	if($row['matched'] == 0){
		/*mi prenoto sul posto libero */
			
			$sql2="UPDATE Lobby SET matched = 1, id_matched=" . $playerID .", name_matched='" . $userName . "' WHERE ID like " . $row['ID'] . "  " ;
			$result=$conn->query($sql2);
			$player2name=$row['Name'];
			$player2ID=$row['ID'];
			
			/*$sql3="DELETE FROM Lobby WHERE ID like " . $player2ID ."";
			$result=$conn->query($sql3);
			$sql4="DELETE FROM Lobby WHERE ID like " . $row['id_matched'] ."";
			$result=$conn->query($sql4);*/
			
			//$result=1;
			$partner=1;
		break;
	}//nè libero nè creato istanza mia quindi la creo....
			
	
}

if($partner==0){  //json con risultato negativo
	$sql="INSERT INTO Lobby (ID,Name,matched) VALUES (" . $playerID . " , '" . $userName . "',0)";
	$result=$conn->query($sql);
	$result=0;
	
	
	
	
}else{
// json positivo

	$result=1;
	
	
}
	
	
}
else if($immatched==0){ // ho già istanza e non ho trovato matches
$result=0;

if($tableName=="delete"){
	$sql3="DELETE FROM Lobby WHERE ID like " . $player2ID ."";
	$result=$conn->query($sql3);
	
	
	
	
}




}
else if($immatched==1){  

	

// invio i dati del player con cui sono matchato
	$sql="SELECT * FROM Lobby WHERE ID =" . $playerID . " ORDER BY id_lobby ASC";
	$risult=$conn->query($sql);
	while($row = $risult->fetch_assoc()){
			$player2name=$row['name_matched'];
			$player2ID=$row['id_matched'];
			
}
			$result=1;



	
	
}
	
	}


$parameters = array('Result' => $result, 'player2Id' => $player2ID, 'player2Name' => $player2name);
header('Content-Type: application/json');
echo json_encode($parameters, TRUE);

$debug=json_encode($parameters, TRUE);

/*$tutto2=urlencode(print_r($update,true));
 $ch="https://api.telegram.org/bot327896522:AAHkphgF6oJJ-cgpvyEvepsZvACKlYS-aP4/sendMessage?disable_web_page_preview=true&chat_id=-208198635&text=" . $debug;
	  $handle = curl_init($ch);
      curl_setopt($handle, CURLOPT_RETURNTRANSFER, true);
curl_setopt($handle, CURLOPT_CONNECTTIMEOUT, 5);
curl_setopt($handle, CURLOPT_TIMEOUT, 7);
      curl_exec($handle);  */ 
 


?>