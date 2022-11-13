# Guião de Demonstração


## 1. Preparação do sistema

Para testar o sistema e todos os seus componentes, é necessário preparar um ambiente com dados para proceder à verificação dos testes.

### 1.1. Lançar o contrato feito pelos ficheiros proto:

Para lançar o contrato basta correr o comando
`$ mvn clean install"` (Linux).


### 1.2. Lançar e compilar o *turmas*

Para lançar o *turmas*, ir à pasta `ClassServer` e correr o comando
`$ mvn clean compile -q exec:java -Dexec.args="localhost 5000 P"` (Linux).

### 1.3. Compilar o resto do projeto

Para compilar e instalar todos os módulos e suas dependências basta ir as pastas Professor, Student e Admin e fazer respetivamente:

Na pasta Student por ex. :
```
$ mvn clean compile -q exec:java -Dexec.args="aluno0012 Joaquim Freire"
```

Na pasta Admin:
```
$ mvn clean compile -q exec:java -Dexec.args=""
```

Na pasta Professor:
```
$ mvn clean compile -q exec:java -Dexec.args=""
```


### 1.4. Lançar e testar o *turmas*

Para proceder aos testes, é preciso em primeiro lugar lançar o servidor *turmas* .
Para isso basta ir à pasta *ClassServer* e executar:

```sh
$ mvn clean compile -q exec:java -Dexec.args="localhost 5000 P"
```

Este comando vai colocar o *turmas* no endereço *localhost* e no porto *5000*.


Para executar toda a bateria de testes de integração, fazer:

```sh
$ bash run.bash
```

Todos os testes devem ser executados sem erros.


## 2. Teste dos comandos

Nesta secção vamos correr os comandos necessários para testar todas as operações do sistema.
Cada subsecção é respetiva a cada operação presente no *hub*.

### 2.1. *enroll*

```sh
> enroll
The action completed successfully.
> enroll
The student is already enrolled.
> enroll
Enrollments are already closed.
> enroll
The class has reached its maximum capacity.
```

### 2.2 *list*
```sh
>list
ClassState{
	capacity=0,
	openEnrollments=false,
	enrolled=[],
	discarded=[]
}
```

### 2.2 *dump*
```sh
>list
ClassState{
	capacity=0,
	openEnrollments=false,
	enrolled=[],
	discarded=[]
}
```

### 2.3 *cancelEnrollment*

```sh
> cancelEnrollment aluno0012
The action completed successfully.
> cancelEnrollment aluno0012
The student does not exist.
> cancelEnrollment
Unknown error.
```

### 2.4 *openEnrollments*

```sh
> openEnrollments 50
The action completed successfully.
> openEnrollments 50
Enrollments are already open.
> openEnrollments
Unknown error.
```

### 2.5 *closeEnrollments*

```sh
> > closeEnrollments 
The action completed successfully.
> closeEnrollments 
Enrollments are already closed.
```

----

## Parte 2


Para testar o sistema e todos os seus componentes, é necessário preparar um ambiente com dados para proceder à verificação dos testes.
O procedimento é igual á parte 1, o que muda é que agora é necessário o seguinte:

### 1.1. Lançar e compilar o *turmas primario*

Para lançar o *turmas*, ir à pasta `ClassServer` e correr o comando
`$ mvn clean compile -q exec:java -Dexec.args="localhost 2001 P"` (Linux).

### 1.2. Lançar e compilar o *turmas secundario*

Para lançar o *turmas*, ir à pasta `ClassServer` e correr o comando
`$ mvn clean compile -q exec:java -Dexec.args="localhost 2002 S"` (Linux).

### 1.3. Lançar e compilar o *NamingServer*

Para lançar o *NamingServer*, ir à pasta `Namingserver` e correr o comando
`$ mvn clean compile -q exec:java -Dexec.args="localhost 5000"` (Linux).

Na pasta Student por ex. :
```
$ mvn clean compile -q exec:java -Dexec.args="aluno0012 Joaquim Freire"
```

Na pasta Admin:
```
$ mvn clean compile -q exec:java -Dexec.args=""
```

Na pasta Professor:
```
$ mvn clean compile -q exec:java -Dexec.args=""
```

## 2. Teste dos comandos

Nesta secção vamos correr os comandos necessários para testar todas as operações do sistema.
Cada subsecção é respetiva a cada operação presente no *hub*.

### 2.1.1 *lookup*

```sh
> lookup
```

### 2.1. *enroll*

```sh
> enroll
The action completed successfully.
> enroll
The student is already enrolled.
> enroll
Enrollments are already closed.
> enroll
The class has reached its maximum capacity.
> enroll
The server you contacted does not support writes.
> enroll
The server is down.
```

### 2.2 *list*
```sh
>list
ClassState{
	capacity=0,
	openEnrollments=false,
	enrolled=[],
	discarded=[]
}
> list
The server is down.
```

### 2.2 *dump*
```sh
> dump
ClassState{
	capacity=0,
	openEnrollments=false,
	enrolled=[],
	discarded=[]
}
> dump
The server is down.
```

### 2.3 *cancelEnrollment*

```sh
> cancelEnrollment aluno0012
The action completed successfully.
> cancelEnrollment aluno0012
The student does not exist.
> cancelEnrollment
Unknown error.
> cancelEnrollment
The server you contacted does not support writes.
> cancelEnrollment
The server is down.
```

### 2.4 *openEnrollments*

```sh
> openEnrollments 50
The action completed successfully.
> openEnrollments 50
Enrollments are already open.
> openEnrollments
Unknown error.
> openEnrollments 50
The server you contacted does not support writes.
> openEnrollments 50
The server is down.
```

### 2.5 *closeEnrollments*

```sh
> > closeEnrollments 
The action completed successfully.
> closeEnrollments 
Enrollments are already closed.
> closeEnrollments
The server you contacted does not support writes.
> closeEnrollments
The server is down.
```

## 4. Considerações Finais

Estes testes não cobrem tudo, pelo que devem ter sempre em conta os testes fornecidos e o código.