<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>FileStore Upload Page</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
	integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ=="
	crossorigin="anonymous">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"
	integrity="sha384-aUGj/X2zp5rLCbBxumKTCw2Z50WgIr1vs/PFN4praOTvYXWlVyh2UtNUU0KAUhAX"
	crossorigin="anonymous">
</head>
<body>
	<div class="container-fluid">
		<div class="page-header">
			<h1>
				Send a new file
			</h1>
		</div>
		<form class="form-horizontal" action="../files" method="post"
			enctype="multipart/form-data">
			<div class="form-group">
				<label for="owner" class="col-sm-2 control-label">Your email</label>
				<div class="col-sm-10">
					<input type="email" class="form-control" disabled id="owner" value="<%= session.getAttribute("userEmail")%>">
				</div>
			</div>
			<div class="form-group">
				<label for="receivers" class="col-sm-2 control-label">Receiver
					email</label>
				<div class="col-sm-10">
					<input type="email" class="form-control" id="receivers"
						name="receivers" placeholder="receiver email adress...">
				</div>
			</div>
			<div class="form-group">
				<label for="file" class="col-sm-2 control-label">File</label>
				<div class="col-sm-10">
					<input type="file" id="file" name="file">
				</div>
			</div>
			<div class="form-group">
				<label for="message" class="col-sm-2 control-label">Message</label>
				<div class="col-sm-10">
					<textarea class="form-control" rows="3" id="message" name="message"></textarea>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-default">Send File</button>
				</div>
			</div>
		</form>
	</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"
		integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ=="
		crossorigin="anonymous"></script>
</body>
</html>