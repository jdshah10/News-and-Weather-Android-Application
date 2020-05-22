const express = require('express');
const app = express();
const fetch = require('node-fetch');
const cors = require('cors');
app.use(cors());

app.get('/mainnews/:newsProvider', (req, res) => {
    var newsProvider = req.params.newsProvider;
    var url = '';
    //console.log(newsProvider);
    if (newsProvider == "guardian"){
       // console.log("hello guardian");
        url = `https://content.guardianapis.com/search?api-key=965d3a2b-7ec8-4385-b2f9-e4128230e49d&section=(sport|business|technology|politics)&show-blocks=all`;    
    }
    else if(newsProvider == "nytimes"){
        //console.log("hello ny times");
        url = `https://api.nytimes.com/svc/topstories/v2/home.json?api-key=3LUV4JGbl5KXZ4B98Tae6wE0qb1kJjrV`;
    }
    fetch(url)
    .then(res => res.json())
    .then(data => {
        res.send({data});
    })
    .catch(er => {
        res.redirect('/error');
    });
  });


  app.get('/articleguardian', (req, res) => {
    //console.log("guardian call");
    var articleid = req.query.articleId;
    //console.log(articleid);
    var url = `https://content.guardianapis.com/${articleid}?api-key=965d3a2b-7ec8-4385-b2f9-e4128230e49d&show-blocks=all`;
    //console.log(url);
    fetch(url)
    .then(res => res.json())
    .then(data => {
        res.send({data});
    })
    .catch(er => {
        res.redirect('/error');
    });
  });
  
app.get('/articlenytimes', (req, res) => {
    //console.log("nytimes call");
    var articleid = req.query.articleId;
    //console.log(articleid);
    var url = 'https://api.nytimes.com/svc/search/v2/articlesearch.json?fq=web_url:("' + articleid + '")&api-key=3LUV4JGbl5KXZ4B98Tae6wE0qb1kJjrV';
    //console.log(url);
    fetch(url)
    .then(res => res.json())
    .then(data => {
        res.send({data});
    })
    .catch(er => {
        res.redirect('/error');
    });
});  

  app.get('/searchguardian', (req, res) => {
    var queryword = req.query.queryword;
    //console.log("guardian search");
    var url = `https://content.guardianapis.com/search${queryword}&api-key=965d3a2b-7ec8-4385-b2f9-e4128230e49d&show-blocks=all`;
    fetch(url)
    .then(res => res.json())
    .then(data => {
        res.send({data});
    }) 
    .catch(er => {
        res.redirect('/error');
    });
  });

  app.get('/searchnytimes', (req, res) => {
    var queryword = req.query.queryword;
    //console.log("nytimes search");
    var url = `https://api.nytimes.com/svc/search/v2/articlesearch.json${queryword}&api-key=3LUV4JGbl5KXZ4B98Tae6wE0qb1kJjrV`;
    fetch(url)
    .then(res => res.json())
    .then(data => {
        res.send({data});
    }) 
    .catch(er => {
        res.redirect('/error');
    });
  });
  

    app.get('/sectioncard', (req, res) => {
    var newsSection = req.query.newsSection;
    var newsProvider = req.query.newsProvider;
    //console.log(newsSection);
    var url = '';
        if (newsProvider == "guardian"){
            //console.log("world hello world guardian");
            url = `https://content.guardianapis.com/${newsSection}?api-key=965d3a2b-7ec8-4385-b2f9-e4128230e49d&show-blocks=all`;    
        }
        else if(newsProvider == "nytimes"){
            //console.log("world hello ny times");
            url = `https://api.nytimes.com/svc/topstories/v2/${newsSection}.json?api-key=3LUV4JGbl5KXZ4B98Tae6wE0qb1kJjrV`;
        }
    fetch(url)
    .then(res => res.json())
    .then(data => {
        res.send({data});
    })
    .catch(er => {
        res.redirect('/error');
    });
  });
const port = process.env.PORT || 8081;
app.listen(8081, () => console.log(`on port ${port}: `));