<?php

use GuzzleHttp\Exception\RequestException;
use GuzzleHttp\Psr7\Response;

// Déclaration des constantes
define("TOKEN_DURATION", 24 * 60 * 60); // Combien de seconds est ce que les tokens restent en vie. Présentement 24h

// Inclure le routeur
require_once './includes/Router.php';

// Inclure le 
require_once "./includes/utility.php";
require_once "./includes/json_response.php";
require_once "./includes/middlewares.php";
require_once "./includes/database_connection.php";
require_once "./includes/token.php";

// Instancier le routeur
$router = new Router();

// Instancion des middleware
$jsonMiddleware = new JsonMiddleware();
$fallbackMiddleware = new FallbackMiddleware();
$authMiddleware = new AuthMiddleware();

$router->get("/travels", function (): JsonResponse {
    $pdo = new DatabaseConnection();

    $requete = $pdo->prepare(
        "SELECT * FROM Destinations"
    );
    if (!$requete->execute()) {
        return JsonResponse::internalServerError();
    }
    if ($result = $requete->fetchAll()) {
        return JsonResponse::success(["travels" => $result]);
   
    }else{
        return JsonResponse::notFound();
    }
});

$router->get("/search", function (): JsonResponse {
    $pdo = new DatabaseConnection();
    
    // Decode URL-encoded keywords
    $keywords = urldecode($_GET['search'] ?? '');
    
    // Split keywords into array and prepare for SQL LIKE query
    $searchTerms = explode(' ', $keywords);
    $conditions = [];
    $params = [];
    
    foreach ($searchTerms as $index => $term) {
        if (!empty(trim($term))) {
            $conditions[] = "(description LIKE :term$index OR destination LIKE :term$index)";
            $params[":term$index"] = "%$term%";
        }
    }
    
    if (empty($conditions)) {
        return JsonResponse::badRequest(["message" => "Please provide search keywords"]);
    }
    
    $sql = "SELECT * FROM Destinations WHERE " . implode(' AND ', $conditions);
    
    $requete = $pdo->safeQuery($sql, $params);
    
    if (!$requete) {
        return JsonResponse::internalServerError();
    }
    
    $results = $requete->fetchAll();
    
    if (empty($results)) {
        return JsonResponse::notFound(["message" => "No destinations found matching your search"]);
    }
    
    return JsonResponse::success([
        "travels" => $results
    ]);
});


$router->post("/register", function ($tokenPayload): JsonResponse {

    if (empty($tokenPayload)) {
        return JsonResponse::unauthorized();
    }

    $credentials = explode(":", $tokenPayload);
    if (count($credentials) !== 3) {
        return JsonResponse::unauthorized();
    }
    
    $email = $credentials[0];
    $password = $credentials[1];
    $token = $credentials[2];

    // Instancier la connexion
    $pdo = new DatabaseConnection();

    // Generation de la requete
    $requete = $pdo->safeQuery(
        "SELECT 1 FROM User WHERE username = :username",
        ['username' => $email]
    );
    if (!$requete) {
        return JsonResponse::internalServerError();
    }

    // Récupérer tous les résultats
    if ($requete->fetch()) {
        return JsonResponse::error(409, "Nom d'utilisateur déja utiliser");
    }

    // hash le mot de passe
    $hashedPassword = password_hash($password, PASSWORD_DEFAULT);

    // Generation de la seconde requete
    $requete = $pdo->prepare("INSERT INTO User (username, password) VALUES (:username, :password);");
    if (!$requete->execute(
        [
            'username' => $email,
            'password' => $hashedPassword
        ]
    )) {
        return JsonResponse::internalServerError();
    }

    return JsonResponse::success(["token" => $token]);
}, $authMiddleware);


$router->post("/login", function ($tokenPayload): JsonResponse {
    if (empty($tokenPayload)) {
        return JsonResponse::unauthorized();
    }

    $credentials = explode(":", $tokenPayload);
    if (count($credentials) !== 3) {
        return JsonResponse::unauthorized();
    }
    
    $email = $credentials[0];
    $password = $credentials[1];
    $token = $credentials[2];
    
    
    // Instancier la connexion
    $pdo = new DatabaseConnection();

    $requete = $pdo->safeQuery(
        "SELECT username, password FROM User WHERE username = :username",
        ['username' => $email]
    );
    if (!$requete) {
        return JsonResponse::internalServerError();
    }
    if ($result = $requete->fetch()) {
        if(password_verify($password, $result['password'])){
            return JsonResponse::success(["token" => $token]);
        }else{
            return JsonResponse::unauthorized();
        }
    }else{
        return JsonResponse::notFound();
    }
}, $authMiddleware);

// Acheminer la requête
$router->dispatch($_SERVER['REQUEST_URI'], $_SERVER['REQUEST_METHOD']);
