package org.collectionjson.model

import java.net.URI

case class CollectionJson(collection: Collection)

case class Collection(
    version: String = "1.0", 
    href: URI,
    links: Seq[Link] = Seq.empty, //not supported
    items: Seq[Item] = Seq.empty,
    queries: Seq[Query] = Seq.empty,
    template: Option[Template] = None,
    error: Option[Error] = None)
    
case class Error(title: String, code: String, message: String)

case class Template(data: Seq[Data])

case class Item(href: URI, data: Seq[Data] = Seq.empty, links: Seq[Link] = Seq.empty)

case class Data(prompt: Option[String] = None, name: String, value: Option[Any] = None)

case class Query(
    href: URI, 
    rel: String,
    name: Option[String] = None, 
    prompt: Option[String] = None, 
    data: Seq[QueryData] = Seq.empty)

case class QueryData(name: String, value: String)

case class Link(href: URI, rel: String, name: Option[String] = None, render: Option[String] = None, prompt: Option[String] = None)
