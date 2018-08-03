<?php
	include('config.php');
	$content = file_get_contents("php://input"); //get all data sent
	$update = json_decode($content, true);  //decode dat juicy jsonObject
	
	
	//no update , no need to continue
	if(!$update)
	{
	  exit;
	}
	
	$pawnId = isset($update['pawnId']) ? $update['pawnId'] : "";
	$playerId = isset($update['playerId']) ? $update['playerId'] : "";
	$op = isset($update['op']) ? $update['op'] : "";
	$pos = isset($update['pos']) ? $update['pos'] : "";
	$gameId = isset($update['gameId']) ? $update['gameId'] : "";
	$valToUpdate = isset($update['valToUpdate']) ? $update['valToUpdate'] : "";
	
	
	
	if($op == "") // if checking what kind of operation i'm requesting
	{
		$sql="" ; 
		//...
		$parameters = array('1' => $..., ... );
		
	}
	
if($op == "SendPawnPosition") // example returning the position of a pawn.
	{
		$sql="SELECT * FROM Pawn WHERE ID_PLayer like '". $playerId . "' and Pawn_id like '". $pawnId . "'" ; 	
		$result=$conn->query($sql);
		while($row = $result->fetch_assoc()){
		$pawnPos=$row['Position'];
											}
											
		$parameters = array("pawnPos" => $pawnPos, "pawnId" => $pawnId , "playerId" => $playerId); // buildin the Response JSON

		
	}
	
	if($op == "") 
	{
		$sql="" ; 
		//...
		
		$parameters = array('1' => $..., ... );

	}

//response encoding

header('Content-Type: application/json');
echo json_encode($parameters, TRUE);


/*È UN VERO WALT DISNEY SOLO SE HA LA GARANZIA DI AUTENTICITÀ DELLA QUALITÀ DELL'OLOGRAMMA ARGENTATO 
ED IL MARCHIO UNIVIDEO PRESENTI SULLA CONFEZIONE.
 NON ACCETTATE I FALSI, ESIGETE SEMPRE E SOLO VIDEOCASSETTE ORIGINALI WALT DISNEY HOME VIDEO.*/
?>