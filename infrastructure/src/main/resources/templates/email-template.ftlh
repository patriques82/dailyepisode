<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Dailyepisode notification</title>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" type='text/css' integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous" />
</head>
<body>

    <div class="container">
        <div class="header lead">
            <p class="greeting">Dear ${username},</p>
            <p class="message">${nrOfUpdates} of your subscriptions have been updated recently.</p>
        </div>
        <div class="subscriptions">
            <#list subscriptions as subscription>
                <div class="row">
                    <div style="padding: 20px; margin-bottom: 30px; border: 1px solid #adadad;">
                        <img class="float-left" src="${subscription.imageUrl}" alt="img" />
                        <div class="caption">
                            <h3><a href="${subscription.homepage}">${subscription.name}</a> <small>(${subscription.firstAirDate} - ${subscription.lastAirDate})</small></h3>
                            <details>
                                <summary>release information</summary>
                                <div class="well">
                                    <p class="last-air-date">last air date: ${subscription.lastAirDate}</p>
                                    <p class="info">nr of episodes: ${subscription.numberOfEpisodes}</p>
                                    <p class="info">nr of seasons: ${subscription.numberOfSeasons}</p>
                                    <p class="info">updated: ${subscription.updatedAt}</p>
                                </div>
                            </details>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </div>

</body>
</html>