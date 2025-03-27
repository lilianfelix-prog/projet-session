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

$router->post("/register", function ($bodyArray): JsonResponse {

    if (
        empty($bodyArray["username"]) || empty($bodyArray["password"]) ||
        !is_string($bodyArray["username"]) || !is_string($bodyArray["password"])
    ) {
        return JsonResponse::badRequest();
    }
    $username = trim($bodyArray["username"]);
    $password = trim($bodyArray["password"]);

    $error = array_merge(validateUsername($username), verifyPassword($password));
    if (!empty($error)) {
        return JsonResponse::badRequest(["reason" => $error]);
    }

    // Instancier la connexion
    $pdo = new DatabaseConnection();

    // Generation de la requete
    $requete = $pdo->safeQuery(
        "SELECT 1 FROM User WHERE username = :username",
        ['username' => $username]
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
            'username' => $username,
            'password' => $hashedPassword
        ]
    )) {
        return JsonResponse::internalServerError();
    }

    return JsonResponse::success();
}, $jsonMiddleware);


$router->post("/login", function ($tokenPayload): JsonResponse {
    if (empty($tokenPayload)) {
        return JsonResponse::unauthorized();
    }

    $credentials = explode(":", $tokenPayload);
    if (count($credentials) !== 2) {
        return JsonResponse::unauthorized();
    }
    
    $email = $credentials[0];
    $password = $credentials[1];
    
    
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
            return JsonResponse::success();
        }else{
            return JsonResponse::unauthorized();
        }
    }else{
        return JsonResponse::notFound();
    }
}, $authMiddleware);

// Acheminer la requête
$router->dispatch($_SERVER['REQUEST_URI'], $_SERVER['REQUEST_METHOD']);
