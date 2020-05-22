const express = require('express');
const app = express();
const fetch = require('node-fetch');
const cors = require('cors');
app.use(cors());
const googleTrends = require('google-trends-api'); 

app.get('/homelatestnews', (req, res) => {
    var url = "https://content.guardianapis.com/search?order-by=newest&show-fields=starRating,headline,thumbnail,short-url&api-key=965d3a2b-7ec8-4385-b2f9-e4128230e49d";
    fetch(url)
    .then(res => res.json())
    .then(function(data){
        let results = data.response.results;
        //console.log(results);
        let cards = results.map(newsData => {
            let cardObj = {id:newsData.id, imagecheck:newsData.fields.thumbnail, title:newsData.webTitle, 
                            date:newsData.webPublicationDate, source:newsData.sectionName,
                            url:newsData.webUrl}

                            return cardObj;
        });
        //console.log(cards);
        res.send({cards});
    })
    .catch(er => {
        res.redirect('/error')
    });
});

app.get('/headlinessectioncard', (req, res) => {
    var newsSection = req.query.newsSection;
    //console.log(newsSection);
    var url = `https://content.guardianapis.com/${newsSection}?api-key=965d3a2b-7ec8-4385-b2f9-e4128230e49d&show-blocks=all`;    
    fetch(url)
    .then(res => res.json())
    .then(function(data) {
        let results = data.response.results;
        //console.log(results);
        var imagelink = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
        
        let cards = results.map(newsData => {
            if(newsData.title!="" && newsData.webUrl!="" && newsData.webPublicationDate!=""){
                if(newsData.blocks.main){
                    for(var i = 0 ; i < newsData.blocks.main.elements[0].assets.length ; i++){
                    
                        if(newsData.blocks.main.elements[0].assets && newsData.blocks.main.elements[0].assets[i].typeData.width >= 499 && newsData.blocks.main.elements[0].assets[i].typeData.width <= 2000){
                            imagelink = newsData.blocks.main.elements[0].assets[i].file;
                        }
                    }        
                }
                
            let cardObj = {id:newsData.id, title:newsData.webTitle, source:newsData.sectionName,
                            url:newsData.webUrl, date:newsData.webPublicationDate,
                            imagecheck:imagelink, url:newsData.webUrl}
                            imagelink = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                            return cardObj;
            }
            
        });
        //console.log(cards);
        res.send({cards});
    })
    .catch(er => {
        res.redirect('/error');
    });
});

app.get('/detailedarticle', (req, res) => {
    //console.log("guardian call");
    var articleid = req.query.articleId;
    //console.log(articleid);
    var url = `https://content.guardianapis.com/${articleid}?api-key=965d3a2b-7ec8-4385-b2f9-e4128230e49d&show-blocks=all`;
    console.log(url);
    fetch(url)
    .then(res => res.json())
    .then(function(data) {
        let newsData = data.response.content;
        console.log(newsData);
        var imagelink = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
        if(newsData.blocks.main && newsData.blocks.main.elements[0] && newsData.blocks.main.elements[0].assets.length > 0){
            imagelink = newsData.blocks.main.elements[0].assets[newsData.blocks.main.elements[0].assets.length-1].file;
        }
        // else{
        //     imagelink = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
        // }
        let desc="";
        if(newsData.blocks.body && newsData.blocks.body.length > 0){
            for(var j = 0 ; j < newsData.blocks.body.length ; j++){
                desc += newsData.blocks.body[j].bodyHtml;
            }
        }
            
            let cardObj = {id:newsData.id, title:newsData.webTitle, source:newsData.sectionName,
                            url:newsData.webUrl, date:newsData.webPublicationDate,
                            imagecheck:imagelink, description:desc}
                            //return cardObj;
            
            console.log(cardObj);
            res.send(cardObj);                
        })
        //console.log(cards);
    .catch(er => {
        res.redirect('/error');
    });
  });

  app.get('/search', (req, res) => {
    var queryword = req.query.queryword;
    //console.log(queryword);
    var url = `https://content.guardianapis.com/search?q=${queryword}&api-key=965d3a2b-7ec8-4385-b2f9-e4128230e49d&show-blocks=all`;
    //console.log(url);
    fetch(url)
    .then(res => res.json())
    .then(data => {
        let results = data.response.results;
        //console.log(results);
        var imagelink = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
    
        let cards = results.map(newsData => {
            if(newsData.title!="" && newsData.webUrl!=""&& newsData.blocks.main && newsData.blocks.main && newsData.webPublicationDate!=""){
                for(var i = 0 ; i < newsData.blocks.main.elements[0].assets.length ; i++){
                    if(newsData.blocks.main.elements[0].assets && newsData.blocks.main.elements[0].assets[i].typeData.width >= 499 && newsData.blocks.main.elements[0].assets[i].typeData.width <= 2000){
                        imagelink = newsData.blocks.main.elements[0].assets[i].file;
                        
                    
                    }
                }
            let cardObj = {id:newsData.id, title:newsData.webTitle, source:newsData.sectionName,
                            url:newsData.webUrl, date:newsData.webPublicationDate,
                            imagecheck:imagelink, url:newsData.webUrl}
                            imagelink = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                            return cardObj;
            }
            
        });
        //console.log(cards);
        res.send({cards});
    }) 
    .catch(er => {
        res.redirect('/error');
    });
  });

app.get('/googlesearchtrends', (req, res) =>{
    let keywordtosearch = req.query.q;
    googleTrends.interestOverTime({keyword: keywordtosearch, startTime: new Date('2019-06-01')})
  .then(function(results){
      let data = JSON.parse(results);
      let data1 = data.default.timelineData;
      let valuearray = [];
      for (var i = 0 ; i < data1.length ; i++){
          valuearray[i] = data1[i].value[0]
      }
    //console.log(results);
    res.send({valuearray});
  })
  .catch(function(err){
    //console.error(err);
  });
});
  

const port = process.env.PORT || 8081;
app.listen(8081, () => console.log(`on port ${port}: `));