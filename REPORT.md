
## Explicação

O Algoritmo usado funciona da seguinte maneira ao chegar uma update de uma replica (servidor) verifica-se o seguinte:

## 1º 
Se o servidor for secundario e ele estiver closed e ele receber de um Primario os OpenEnrollments true e estiverem a false o atual,
as listas dos discarded de ambas ficam vazias, como tal, isto ira evitar o aluno n se poder inscrever devido a estar
na lista de discard.

##2º
se o servidor for secundario e receber de um servidor primario um close ele fecha as inscriçoes


##3º
Se a capacidade for diferente entre ambos escolhe-se a do maior e igual-se ambas a do maior, de modo, sempre a previligiar uma turma com maior capacidadem e aplica-se o restante


##4º
Se a Capacidade igual for igual

###4.1 
Se ambos estiverem em closed mas as listas discard diferentes então junta-se os diferentes

###4.2 
Se algum aluno estiver em discarded em algum dos servers ent fica em discarded por omissao

###4.3
Se ambos tiverem numero de enrolled igual (ou seja as listas têm o mesmo tamanho) junta-se todos os alunos enrolled numa lista e escolhe-se n (numero da capacity) numero de alunos random para enrolled e os restantes ficam na lista discarded

###4.4 
Caso sejam diferentes o tamanho, escolhe-se quem tem maior numero de enrolled e subtrai-se a capacity
se o resultado for 0 então os enrolled do menor sao todos discarded, senão vai se buscar os n primeiros alunos ao enrolled do mais pequeno
e os restantes são discarded

## Funcionamento entre os servidores
O servidor primário manda sempre updates a todos os servidores secundarios enquanto que os secundários apenas mandam para o primário
