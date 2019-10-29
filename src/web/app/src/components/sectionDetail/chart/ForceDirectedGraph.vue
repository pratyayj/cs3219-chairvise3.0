<template>
  <div>
    <svg width="800" height="500"></svg>
  </div>
</template>

<script>
  import * as d3 from 'd3';

  export default {
    props: {
      chartData: {
        type: Object,
        required: true
      },
    },
    methods: {
     handleVisualisation() {
      var svg = d3.select(this.$el.querySelector("svg")),
       width = +svg.attr("width"),
       height = +svg.attr("height");

      var radius = 15;

      var links_data = this.chartData.labels.links;
      links_data = JSON.parse(JSON.stringify(links_data));

      var nodes_data = this.chartData.labels.nodes;

      //set up the simulation and add forces
      var simulation = d3.forceSimulation()
       .nodes(nodes_data);

      var link_force =  d3.forceLink(links_data)
       .id(function(d) { return d.name; });

      var charge_force = d3.forceManyBody()
       .strength(-100);

      var center_force = d3.forceCenter(width / 2, height / 2);

      simulation
       .force("charge_force", charge_force)
       .force("center_force", center_force)
       .force("links",link_force);

//add tick instructions:
      simulation.on("tick", tickActions );

//add encompassing group for the zoom
      var g = svg.append("g")
       .attr("class", "everything");

//draw lines for the links
      var link = g.append("g")
       .attr("class", "links")
       .selectAll("line")
       .data(links_data)
       .enter().append("line")
       .attr("stroke-width", 2)
       .style("stroke", linkColour);

//draw circles for the nodes
      var node = g.append("g")
       .attr("class", "nodes")
       .selectAll("circle")
       .data(nodes_data)
       .enter()
       .append("circle")
       .attr("r", radius)
       .attr("fill", circleColour);

      var text = g.selectAll("text")
       .data(nodes_data)
       .enter().append("text")
       .attr("x", 8)
       .attr("y", ".31em")
       .text(function(d) { return d.name.substring(0, 3); });


//add drag capabilities
      var drag_handler = d3.drag()
       .on("start", drag_start)
       .on("drag", drag_drag)
       .on("end", drag_end);

      drag_handler(node);


//add zoom capabilities
      var zoom_handler = d3.zoom().interpolate(d3.interpolateZoom)
       .on("zoom", zoom_actions);

      zoom_handler(svg);

      /** Functions **/

//Function to choose what color circle we have
//Let's return blue for males and red for females
      function circleColour(d){
       if(d.type ==="article"){
        return "blue";
       } else {
        return "pink";
       }
      }

//Function to choose the line colour and thickness
//If the link type is "A" return green
//If the link type is "E" return red
      function linkColour(d){
       if(d.type === "accept"){
        return "green";
       } else {
        return "red";
       }
      }

//Drag functions
//d is the node
      function drag_start(d) {
       if (!d3.event.active) simulation.alphaTarget(0.3).restart();
       d.fx = d.x;
       d.fy = d.y;
      }

//make sure you can't drag the circle outside the box
      function drag_drag(d) {
       d.fx = d3.event.x;
       d.fy = d3.event.y;
      }

      function drag_end(d) {
       if (!d3.event.active) simulation.alphaTarget(0);
       d.fx = null;
       d.fy = null;
      }

//Zoom functions
      function zoom_actions(){
       /*var old = d3.event.transform.k;
       console.log("Before: " + d3.event.transform);
       if (d3.event.transform.k < current_zoom) {
         current_zoom *= zoom_modifier;
         d3.event.transform.k = current_zoom;
       } else if (d3.event.transform.k > current_zoom) {
         current_zoom /= zoom_modifier;
         d3.event.transform.k = current_zoom;
       }
       console.log("After: " + d3.event.transform);*/
       //scale = d3.event.scale /2;
       //zoom_handler.scale(scale);
       g.attr("transform", d3.event.transform)
       //d3.event.transform.k = old;
      }

      function tickActions() {
       //update circle positions each tick of the simulation
       node
        .attr("cx", function(d) { return d.x; })
        .attr("cy", function(d) { return d.y; });

       //update link positions
       link
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

       //text positions
       text
        .attr("x", function(d) { return d.x; })
        .attr("y", function(d) { return d.y; })
      }
     }
    },
   watch: {
    chartData() {
     if(this.$el.querySelector("svg").children.length > 0) {
      console.log(this.$el.querySelector("svg").children[0].remove());
     }
     console.log(this.chartData);
     this.handleVisualisation();
    }
   }
  }
</script>

<style scoped>
  .links line {
    stroke: #999;
    stroke-opacity: 0.6;
  }

  .nodes circle {
    stroke: black	;
    stroke-width: 0px;
  }
</style>