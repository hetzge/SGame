use std::collections::HashSet;
use std::collections::HashMap;
use std::hash::{hash, Hash, SipHasher};
use std::fmt::Display;
use std::fmt::Formatter;
use std::result::Result;
use std::fmt::Error;

enum Orientation{
    UP, DOWN, LEFT, RIGHT
}

impl Orientation{
    fn factor(&self) -> Coordinate{
        match *self{
            Orientation::UP => Coordinate{ x: 0, y: -1 },
            Orientation::DOWN => Coordinate{ x: 0, y: 1 },
            Orientation::LEFT => Coordinate{ x: -1, y: 0 },
            Orientation::RIGHT => Coordinate{ x: 1, y: 0 }
        }
    }

    fn name(&self) -> String{
        match *self{
            Orientation::UP => "UP",
            Orientation::DOWN => "DOWN",
            Orientation::LEFT => "LEFT",
            Orientation::RIGHT => "RIGHT"
        }
    }
}

impl Display for Orientation{
    fn fmt(&self, f: &mut Formatter) -> Result<(), Error> {
        write!(f, "{}", "Orientation")
    }
}

#[derive(Hash)]
struct Coordinate{
    x: usize,
    y: usize
}

impl PartialEq for Coordinate{
    fn eq(&self, other: &Coordinate) -> bool{
        return self.x == other.x && self.y == other.y;
    }
}


fn main(){
    let collision = vec![vec![false, false, false], vec![false, true, false], vec![false, false, false]];
    find_path(Coordinate{ x: 0, y: 0 }, Coordinate{ x: 2, y: 2 }, collision);
}

fn find_path(start: Coordinate, goall: Coordinate, collision: Vec<Vec<bool>>){
    let next: Vec<Coordinate> = vec![];
    let done: HashMap<Coordinate, usize> = HashMap::new();

    let mut current = Coordinate{ .. start };


    println!("{}", Orientation::UP.factor());



    println!("Find path from {} {}", start.x, start.y);




}

fn is_on_collision(coordinate: Coordinate, collision: Vec<Vec<bool>>) -> bool{

    let x_size = collision.len();
    let inner_array = collision[0];
    let y_size = inner_array.len();

    return coordinate.x > 0 && coordinate.x < x_size && coordinate.y > 0 && coordinate.y < y_size;
}
