-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Host: mysql22j12.db.hostpoint.internal
-- Creato il: Lug 28, 2018 alle 18:13
-- Versione del server: 10.1.33-MariaDB
-- Versione PHP: 5.6.35

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `psionoff_LDC`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `Partita`
--

CREATE TABLE `Partita` (
  `ID` int(11) NOT NULL,
  `Player1` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Pass+Name (md5)',
  `Player2` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Pass+Name (md5)',
  `Position1` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `Position2` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `Score1` int(11) NOT NULL,
  `Score2` int(11) NOT NULL,
  `Color1` int(11) NOT NULL,
  `Color2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `Users`
--

CREATE TABLE `Users` (
  `ID` int(11) NOT NULL,
  `Name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `Password` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `Overall_Score` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `Win` int(11) NOT NULL,
  `Lost` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dump dei dati per la tabella `Users`
--

INSERT INTO `Users` (`ID`, `Name`, `Password`, `Overall_Score`, `Win`, `Lost`) VALUES
(1, 'Mario', '1517a13fe2f30a8c7c87cb097ddc827a', '0', 0, 0);

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `Partita`
--
ALTER TABLE `Partita`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `Partita`
--
ALTER TABLE `Partita`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `Users`
--
ALTER TABLE `Users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
