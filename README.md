# Api de login
API Login created and designed by Acterax (https://github.com/Acterax) &amp; AntoZzz 

Cette api de login a été conçu pour résister a de grande attaque de type LAYER 7 
(20k/s pour nos test sur 2 serveurs avec haProxy, nous ne sommes pas monté plus haut à cause du coût de la machine d'attaque).

## Comment elle marche ?

Cette api peut marcher sur un seul serveur. Mais vous obtiendez plus de performance a la faire marcher sur plusieurs serveurs
Elle utilise les technologies MySQL en réplication, Redis et VertX

## Infrastructure conseillé

Nous vous conseillons de crée un ROUND ROBIN DNS ou d'utilisé HaProxy pour réaliser votre infrastructure
