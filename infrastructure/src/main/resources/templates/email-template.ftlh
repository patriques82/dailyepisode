<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Dailyepisode notification</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css' />

    <style type="text/css">
        body {
            font-family: 'Roboto', sans-serif;
            font-size: 28px;
            margin: 0;
            padding: 0;
        }
    </style>
</head>
<body>
    <div class="header">
        <p>Dear ${username},</p>
        <p>${nrOfUpdates} of your subscriptions have been updated recently.</p>
    </div>
    <div class="subscriptions">
        <ul class="list">
            <#list subscriptions as subscription>
                <li class="item">
                    <div class="subscription">
                        ${subscription.name}
                    </div>
                </li>
            </#list>
        </ul>
    </div>
</body>
</html>